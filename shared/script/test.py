#! /usr/bin/env python3

import argparse, glob, os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-shared')
  parser.add_argument('--skija-native')
  (args, _) = parser.parse_known_args()

  if args.skija_shared:
    skija_shared = os.path.abspath(args.skija_shared)
  if args.skija_native:
    skija_native = os.path.abspath(args.skija_native)

  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))

  if not args.skija_shared:
    skija_shared = 'target/classes'
  if not args.skija_native:
    skija_native = '../native/build'

  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.20'),
    common.fetch_maven('org.jetbrains', 'annotations', '20.1.0'),
    skija_shared,
    skija_native,
  ]
  sources = common.glob('src/test/java', '*.java')
  common.javac(classpath, sources, 'target/test-classes')

  common.check_call([
    'java',
    '--class-path', common.classpath_separator.join(classpath + ['target/test-classes']),
    '-ea',
    '-esa',
    '-Xcheck:jni',
    'org.jetbrains.skija.TestSuite',
  ])

  common.popd()
  return 0

if __name__ == '__main__':
  sys.exit(main())