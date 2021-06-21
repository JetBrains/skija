#! /usr/bin/env python3
import argparse, build, common, glob, os, sys

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-version')
  (args, _) = parser.parse_known_args()

  modulepath = []
  if args.skija_version:
    modulepath += [
      common.fetch_maven('org.jetbrains.skija', 'skija-shared', args.skija_version, repo=common.space_skija),
      common.fetch_maven('org.jetbrains.skija', 'skija-' + common.classifier, args.skija_version, repo=common.space_skija)
    ]
  else:
    build.main()
    modulepath += ['../shared/target/classes', '../platform/target/classes']

  os.chdir(common.root + '/tests')
  sources = common.glob('java', '*.java')
  common.javac(sources, 'target/classes', modulepath = modulepath, add_modules = [common.module])

  common.check_call([
    'java',
    # '--class-path', common.classpath_separator.join(modulepath + ['target/classes']),
    '--class-path', 'target/classes',
    '--module-path', common.classpath_separator.join(modulepath),
    '--add-modules', common.module
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
