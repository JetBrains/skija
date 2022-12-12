#! /usr/bin/env python3
import argparse, build, common, glob, os, sys

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-version')
  (args, _) = parser.parse_known_args()

  classpath = []
  if args.skija_version:
    classpath += [
      common.fetch_maven('org.jetbrains.skija', 'skija-shared', args.skija_version, repo=common.space_skija),
      common.fetch_maven('org.jetbrains.skija', 'skija-' + common.classifier, args.skija_version, repo=common.space_skija)
    ]
  else:
    build.main()
    classpath += ['../shared/target/classes-java9', '../shared/target/classes', '../platform/target/classes', '../platform/target/classes']

  os.chdir(common.root + '/tests')
  sources = common.glob('java', '*.java')
  common.javac(sources, 'target/classes', classpath = classpath)

  common.check_call([
    'java',
    '--class-path', common.classpath_separator.join(classpath + ['target/classes']),
    ] + (['-XstartOnFirstThread'] if 'macos' == common.system else [])
    + ['-Djava.awt.headless=true',
    '-enableassertions',
    '-enablesystemassertions',
    '-Xcheck:jni',
    '-Dskija.logLevel=DEBUG',
    'org.jetbrains.skija.test.TestSuite'])

  return 0

if __name__ == '__main__':
  sys.exit(main())
