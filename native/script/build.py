#! /usr/bin/env python3

import argparse, os, platform, shutil, subprocess, sys, urllib.request, zipfile

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--debug', action='store_true')
  parser.add_argument('--skia-dir')
  args = parser.parse_args()

  system = {'Darwin': 'macos', 'Linux': 'linux', 'Windows': 'windows'}[platform.system()]
  machine = {'x86_64': 'x64', 'arm64': 'arm64'}[platform.machine()]
  skia_release = 'm89-15595ea39c'
  build_type = 'Debug' if args.debug else 'Release'

  if args.skia_dir:
    skia_dir = os.path.abspath(args.skia_dir)
    os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))
  else:
    # Fetch Skia
    os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))
    skia_dir = "Skia-" + skia_release + "-" + system + "-" + build_type + '-' + machine
    if not os.path.exists(skia_dir):
      zip = skia_dir + '.zip'
      with open(zip, 'wb') as f:
        url = 'https://github.com/JetBrains/skia-build/releases/download/' + skia_release + '/' + zip
        print("Downloading", url)
        f.write(urllib.request.urlopen(url).read())
      with zipfile.ZipFile(zip, 'r') as f:
        print("Extracting", zip)
        f.extractall(skia_dir)
      os.remove(zip)
    skia_dir = os.path.abspath(skia_dir)
  print("Using Skia from", skia_dir)

  # CMake
  os.makedirs("build", exist_ok = True)
  subprocess.check_call([
    "cmake",
    "-G", "Ninja",
    "-DCMAKE_BUILD_TYPE=" + build_type,
    "--config", build_type,
    "-DSKIA_DIR=" + skia_dir,
    ".."],
    cwd="build")

  # Ninja
  subprocess.check_call(["ninja"], cwd="build")

  # icudtl
  icudtl_src = skia_dir + "/out/" + build_type + "-" + machine + "/icudtl.dat"
  icudtl_dst = "build/icudtl.dat"
  if os.path.exists(icudtl_src) and not os.path.exists(icudtl_dst):
    shutil.copy2(icudtl_src, icudtl_dst)

  return 0

if __name__ == '__main__':
  sys.exit(main())