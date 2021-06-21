#! /usr/bin/env python3
import common, glob, os, subprocess, sys

def main():
  os.chdir(common.root + '/shared')

  print('Generating', 'target/generated-sources/delombok/**.java')
  subprocess.check_call(["java",
    "-jar", common.deps()[0],
    "delombok",
    "java",
    "--module-path", common.classpath_separator.join(common.deps()),
    "-d", "target/generated-sources/delombok/org/jetbrains/skija"
  ])

  if os.path.exists('target/generated-sources/delombok/module-info.java'):
    os.remove('target/generated-sources/delombok/module-info.java')

  print('Generating', '../docs/apidocs/**')
  sources = common.glob('target/generated-sources/delombok', '*.java')
  common.check_call([
    'javadoc',
    '--module-path', common.classpath_separator.join(common.deps()),
    '-d', '../docs/apidocs',
    '-quiet',
    '-Xdoclint:all,-missing']
    + sources)

  return 0

if __name__ == '__main__':
  sys.exit(main())