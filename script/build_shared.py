#! /usr/bin/env python3
import common, glob, os, sys

def main():
  os.chdir(common.root + '/shared')
  modulepath = common.deps()
  sources = common.glob('java', '*.java')
  common.javac(sources, 'target/classes', classpath = modulepath, modulepath = modulepath, release = '9')
  return 0

if __name__ == '__main__':
  sys.exit(main())
