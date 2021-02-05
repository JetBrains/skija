#! /usr/bin/env python3

import argparse, glob, os, platform, shutil, subprocess, sys, urllib.request, zipfile
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..')))
import native.script.build as native_build
import shared.script.build as shared_build
import shared.script.test as shared_test

def main():
  native_build.main()
  shared_build.main()
  shared_test.main()
  return 0

if __name__ == '__main__':
  sys.exit(main())