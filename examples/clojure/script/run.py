#! /usr/bin/env python3
import os, platform, subprocess, sys

def main():
  os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))
  system = {'Darwin': 'macos', 'Linux': 'linux', 'Windows': 'windows'}[platform.system()]
  subprocess.check_call(["clj", "-M:" + system, "-m", "lwjgl.main"])
  return 0

if __name__ == '__main__':
  sys.exit(main())