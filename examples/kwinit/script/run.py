#! /usr/bin/env python3

import argparse, glob, os, platform, shutil, subprocess, sys, urllib.request, zipfile
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..', '..')))
import script.common as common 
import native.script.build as native_build

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-version')
  parser.add_argument('--verbose', action='store_true')
  (args, _) = parser.parse_known_args()

  # Javac
  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.16'),
    common.fetch_maven('com.google.code.gson', 'gson', '2.8.6')
  ]

  if args.skija_version:
    classpath += [
      common.fetch_maven('org.jetbrains.skija', 'skija-shared', args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven'),
      common.fetch_maven('org.jetbrains.skija', common.skija_native_artifact_id, args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven'),
    ]
  else:
    # native_build.main()
    classpath += [
      os.path.join('..', '..', 'native', 'build'),
      os.path.join('..', '..', 'shared', 'target', 'classes')
    ]

  os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))

  sources = common.glob('src_java', '*.java') + common.glob('../scenes/src', '*.java')
  common.javac(classpath, sources, 'target/classes')

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
    '-Dskija.logLevel=DEBUG',
    'noria.kwinit.impl.Main'] + (["--verbose"] if args.verbose else []),
    env=env)

  return 0

if __name__ == '__main__':
  sys.exit(main())