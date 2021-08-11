#! /usr/bin/env python3
import common, os, shutil, sys

def main():
  os.chdir(common.root)
  shutil.rmtree('shared/target', ignore_errors = True)
  shutil.rmtree('platform/build', ignore_errors = True)
  shutil.rmtree('platform/target', ignore_errors = True)
  shutil.rmtree('tests/target', ignore_errors = True)
  shutil.rmtree('examples/lwjgl/target', ignore_errors = True)
  shutil.rmtree('examples/kwinit/target', ignore_errors = True)
  shutil.rmtree('examples/jwm/target', ignore_errors = True)
  shutil.rmtree('examples/swt/target', ignore_errors = True)

  return 0

if __name__ == '__main__':
  sys.exit(main())