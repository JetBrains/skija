#! /usr/bin/env python3
import argparse, glob, os, subprocess, sys, zipfile
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__))))
import build_shared, common

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--debug', action='store_true')
  parser.add_argument('--arch', default=common.arch)
  parser.add_argument('--skia-dir')
  parser.add_argument('--skia-release', default='m93-87e8842e8c')
  parser.add_argument('--skija-version')
  (args, _) = parser.parse_known_args()

  # Fetch Skia
  build_type = 'Debug' if args.debug else 'Release'
  if args.skia_dir:
    skia_dir = os.path.abspath(args.skia_dir)
    os.chdir(common.root + '/platform')
  else:
    os.chdir(common.root + '/platform')
    skia_dir = "Skia-" + args.skia_release + "-" + common.system + "-" + build_type + '-' + common.arch
    if not os.path.exists(skia_dir):
      zip = skia_dir + '.zip'
      common.fetch('https://github.com/JetBrains/skia-build/releases/download/' + args.skia_release + '/' + zip, zip)
      with zipfile.ZipFile(zip, 'r') as f:
        print("Extracting", zip)
        f.extractall(skia_dir)
      os.remove(zip)
    skia_dir = os.path.abspath(skia_dir)
  print("Using Skia from", skia_dir)

  # CMake
  os.makedirs("build", exist_ok = True)
  common.check_call([
    "cmake",
    "-G", "Ninja",
    "-DCMAKE_BUILD_TYPE=" + build_type,
    "-DSKIA_DIR=" + skia_dir,
    "-DSKIA_ARCH=" + common.arch]
    + (["-DCMAKE_OSX_ARCHITECTURES=" + {"x64": "x86_64", "arm64": "arm64"}[common.arch]] if common.system == "macos" else [])
    + [".."],
    cwd=os.path.abspath('build'))

  # Ninja
  common.check_call(["ninja"], cwd=os.path.abspath('build'))

  # Codesign
  if common.system == "macos" and os.getenv("APPLE_CODESIGN_IDENTITY"):
    subprocess.call(["codesign",
                     # "--force",
                     # "-vvvvvv",
                     "--deep",
                     "--sign",
                     os.getenv("APPLE_CODESIGN_IDENTITY"),
                     "build/libskija_" + common.arch + ".dylib"])

  # javac
  modulepath = []
  if args.skija_version:
    modulepath += [
      common.fetch_maven('org.jetbrains.skija', 'skija-shared', args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven')
    ]
  else:
    build_shared.main()
    modulepath += ['../shared/target/classes']

  os.chdir(common.root + '/platform')
  sources = common.glob('java-' + common.classifier, '*.java')
  common.javac(sources, 'target/classes', modulepath = modulepath, release = '9')

  # Copy files
  target = 'target/classes/org/jetbrains/skija'
  if common.classifier == 'macos-x64':
    common.copy_newer('build/libskija_x64.dylib', target + '/macos/x64/libskija_x64.dylib')
  elif common.classifier == 'macos-arm64':
    common.copy_newer('build/libskija_arm64.dylib', target + '/macos/arm64/libskija_arm64.dylib')
  elif common.classifier == 'linux':
    common.copy_newer('build/libskija.so', target + '/linux/libskija.so')
  elif common.classifier == 'windows':
    common.copy_newer('build/skija.dll', target + '/windows/skija.dll')
    common.copy_newer(skia_dir + '/out/' + build_type + '-' + common.arch + '/icudtl.dat',
                      target + '/windows/icudtl.dat')

  return 0

if __name__ == '__main__':
  sys.exit(main())
