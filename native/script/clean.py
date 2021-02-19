#! /usr/bin/env python3
import os, shutil, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common

def main():
  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  shutil.rmtree("build", ignore_errors = True)
  shutil.rmtree(os.path.join(os.path.expanduser('~'), '.m2', 'repository', 'org', 'jetbrains', 'skija', 'skija-native', '0.0.0-SNAPSHOT'), ignore_errors = True)
  common.popd()
  return 0

if __name__ == '__main__':
  sys.exit(main())