#! /usr/bin/env python3

import argparse, glob, os, platform, shutil, subprocess, sys, urllib.request, zipfile
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..', '..')))
import script.common as common
import script.build as build

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-version')
  parser.add_argument('--verbose', action='store_true')
  (args, _) = parser.parse_known_args()

  # Javac
  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.20'),
    common.fetch_maven('com.google.code.gson', 'gson', '2.8.6')
  ]

  if args.skija_version:
    classpath += [
      common.fetch_maven('org.jetbrains.skija', 'skija-shared', args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven'),
      common.fetch_maven('org.jetbrains.skija', 'skija-' + common.classifier, args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven'),
    ]
  else:
    build.main()
    classpath += [
      os.path.join('..', '..', 'platform', 'target', 'classes'),
      os.path.join('..', '..', 'shared', 'target', 'classes')
    ]

  os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))

  sources = common.glob('src_java', '*.java') + common.glob('../scenes/src', '*.java')
  common.javac(sources, 'target/classes', classpath = classpath, release = '16')

  # Rust
  common.check_call(['cargo', 'build', '--release', '--lib'])

  # Java
  env = os.environ.copy()
  env['RUST_BACKTRACE'] = '1'
  # if 'windows' == common.system:
  #   env['KWINIT_ANGLE'] = '1'

  common.check_call([
    'java',
    '--class-path', common.classpath_separator.join(['target/classes'] + classpath)]
    + (['-XstartOnFirstThread'] if 'macos' == common.system else [])
    + ['-Djava.awt.headless=true',
    '-enableassertions',
    '-enablesystemassertions',
    '-Xcheck:jni',
    '-Dskija.logLevel=DEBUG',
    'noria.kwinit.impl.Main'] + (["--verbose"] if args.verbose else []),
    env=env)

  return 0

if __name__ == '__main__':
  sys.exit(main())