#! /usr/bin/env python3

import argparse, os, platform, shutil, subprocess, sys, urllib.request, zipfile
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--debug', action='store_true')
  parser.add_argument('--arch', default=common.arch)
  parser.add_argument('--skia-dir')
  parser.add_argument('--skia-release', default='m91-b99622c05a')
  (args, _) = parser.parse_known_args()

  build_type = 'Debug' if args.debug else 'Release'

  if args.skia_dir:
    skia_dir = os.path.abspath(args.skia_dir)
    common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  else:
    # Fetch Skia
    common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
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

  # icudtl
  icudtl_src = skia_dir + "/out/" + build_type + "-" + common.arch + "/icudtl.dat"
  icudtl_dst = "build/icudtl.dat"
  if os.path.exists(icudtl_src) and not os.path.exists(icudtl_dst):
    shutil.copy2(icudtl_src, icudtl_dst)

  common.popd()
  return 0

if __name__ == '__main__':
  sys.exit(main())