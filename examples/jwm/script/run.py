#! /usr/bin/env python3

import argparse, glob, os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..', '..')))
import script.common as common
import script.build as build

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-version')
  parser.add_argument('--jwm-version', default='0.1.170')
  parser.add_argument('--jwm-dir', default=None)
  (args, _) = parser.parse_known_args()

  # Javac
  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.20'),
    common.fetch_maven('org.jetbrains', 'annotations', '20.1.0')
  ]

  if args.jwm_dir:
    jwm_dir = os.path.abspath(args.jwm_dir)
    classpath += [
      os.path.join(jwm_dir, 'shared', 'target', 'classes'),
      os.path.join(jwm_dir, common.system, 'build'),
      os.path.join(jwm_dir, common.system, 'target', 'classes')
    ]
  else:
    classpath += [
      common.fetch_maven('org.jetbrains.jwm', 'jwm-shared', args.jwm_version, repo='https://packages.jetbrains.team/maven/p/jwm/maven'),
      common.fetch_maven('org.jetbrains.jwm', 'jwm-' + common.system + '-' + common.arch, args.jwm_version, repo='https://packages.jetbrains.team/maven/p/jwm/maven'),
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

  os.chdir(common.root + '/examples/jwm')

  sources = common.glob('src', '*.java') + common.glob('../scenes/src', '*.java')
  common.javac(sources, 'target/classes', classpath = classpath, release = '16')

  # Java
  common.check_call([
    'java',
    '--class-path', common.classpath_separator.join(['target/classes'] + classpath)]
    + (['-XstartOnFirstThread'] if 'macos' == common.system else [])
    + ['-Djava.awt.headless=true',
    '-enableassertions',
    '-enablesystemassertions',
    '-Xcheck:jni',
    '-Dskija.logLevel=DEBUG',
    'org.jetbrains.skija.examples.jwm.Main'])

  return 0

if __name__ == '__main__':
  sys.exit(main())