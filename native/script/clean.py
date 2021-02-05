#! /usr/bin/env python3

import os, pathlib, shutil, sys

def main():
  os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))
  shutil.rmtree("build", ignore_errors = True)
  shutil.rmtree(os.path.join(pathlib.Path.home(), '.m2', 'repository', 'org', 'jetbrains', 'skija', 'skija-native', '0.0.0-SNAPSHOT'), ignore_errors = True)
  return 0

if __name__ == '__main__':
  sys.exit(main())