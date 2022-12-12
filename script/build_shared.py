#! /usr/bin/env python3
import common, glob, os, sys

def main():
  os.chdir(common.root + '/shared')
  modulepath = common.deps()
  sources = common.glob('java', '*.java')
  common.javac(sources, 'target/classes', classpath = modulepath, release = '8')

  java9sources = common.glob('java9', '*.java')
  common.javac(java9sources, 'target/classes-java9', classpath = modulepath, modulepath = modulepath, release = '9', opts = ['--patch-module', 'org.jetbrains.skija.shared=target/classes'])
  return 0

if __name__ == '__main__':
  sys.exit(main())
