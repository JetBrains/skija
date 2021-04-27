#! /usr/bin/env python3
import os, shutil, sys

def main():
  os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))
  shutil.rmtree('.cpcache', ignore_errors = True)
  return 0

if __name__ == '__main__':
  sys.exit(main())