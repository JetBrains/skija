#! /usr/bin/env python3
import os, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..')))
import native.script.clean as native_clean
import shared.script.clean as shared_clean

def main():
  native_clean.main()
  shared_clean.main()
  return 0

if __name__ == '__main__':
  sys.exit(main())