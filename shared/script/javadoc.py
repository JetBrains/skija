#! /usr/bin/env python3

import glob, os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common

def main():
  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))

  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.16'),
    common.fetch_maven('org.jetbrains', 'annotations', '19.0.0')
  ]

  print('Generating', 'target/generated-sources/delombok/**.java')
  subprocess.check_call(['java',
    '--class-path', common.classpath_separator.join(classpath),
    'lombok.launch.Main',
    'delombok',
    'src/main/java',
    '-d', 'target/generated-sources/delombok'])

  print('Generating', '../docs/apidocs/**')
  sources = common.glob('target/generated-sources/delombok', '*.java')
  subprocess.check_call([
    'javadoc',
    '--class-path', common.classpath_separator.join(classpath),
    '-d', '../docs/apidocs',
    '-quiet',
    '-Xdoclint:all,-missing']
    + sources)

  common.popd()
  return 0

if __name__ == '__main__':
  sys.exit(main())