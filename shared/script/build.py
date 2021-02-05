#! /usr/bin/env python3

import glob, os, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common

def main():
  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.16'),
    common.fetch_maven('org.jetbrains', 'annotations', '19.0.0')
  ]
  sources = common.glob('src/main/java', '*.java')
  print('Compiling', 'shared/src/main/java/**/*.java')
  common.javac(classpath, sources, 'target/classes')
  common.popd()
  return 0

if __name__ == '__main__':
  sys.exit(main())