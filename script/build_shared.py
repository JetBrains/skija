#! /usr/bin/env python3
import common, glob, os, sys

def main():
  os.chdir(os.path.dirname(__file__) + '/../shared')
  modulepath = common.deps()
  sources = common.glob('java', '*.java')
  common.javac(sources, 'target/classes', classpath = modulepath, modulepath = modulepath)
  return 0

if __name__ == '__main__':
  sys.exit(main())
