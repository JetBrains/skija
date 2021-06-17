#! /usr/bin/env python3

import glob, os, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common

def main():
  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  modulepath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.20'),
    common.fetch_maven('org.jetbrains', 'annotations', '20.1.0')
  ]
  sources = common.glob('src/main/java', '*.java')
  common.javac(sources, 'target/classes', classpath = modulepath, modulepath = modulepath)
  common.popd()
  return 0

if __name__ == '__main__':
  sys.exit(main())