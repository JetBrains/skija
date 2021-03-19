#! /usr/bin/env python3

import argparse, glob, os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..', '..')))
import script.common as common

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--skija-version')
  (args, _) = parser.parse_known_args()

  # Javac
  lwjgl_classifier = "natives-" + common.system
  classpath = [
    common.fetch_maven('org.projectlombok', 'lombok', '1.18.18'),
    common.fetch_maven('org.lwjgl', 'lwjgl', '3.2.3'),
    common.fetch_maven('org.lwjgl', 'lwjgl-glfw', '3.2.3'),
    common.fetch_maven('org.lwjgl', 'lwjgl-opengl', '3.2.3'),
    common.fetch_maven('org.lwjgl', 'lwjgl', '3.2.3', classifier=lwjgl_classifier),
    common.fetch_maven('org.lwjgl', 'lwjgl-glfw', '3.2.3', classifier=lwjgl_classifier),
    common.fetch_maven('org.lwjgl', 'lwjgl-opengl', '3.2.3', classifier=lwjgl_classifier)
  ]

  if args.skija_version:
    classpath += [
      common.fetch_maven('org.jetbrains.skija', 'skija-shared', args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven'),
      common.fetch_maven('org.jetbrains.skija', common.skija_native_artifact_id, args.skija_version, repo='https://packages.jetbrains.team/maven/p/skija/maven'),
    ]
  else:
    classpath += [
      os.path.join('..', '..', 'native', 'build'),
      os.path.join('..', '..', 'shared', 'target', 'classes')
    ]

  os.chdir(os.path.join(os.path.dirname(__file__), os.pardir))

  sources = common.glob('src', '*.java') + common.glob('../scenes/src', '*.java')
  common.javac(classpath, sources, 'target/classes')

  # Java
  common.check_call([
    'java',
    '--class-path', common.classpath_separator.join(['target/classes'] + classpath)]
    + (['-XstartOnFirstThread'] if 'macos' == common.system else [])
    + ['-Djava.awt.headless=true',
    '-enableassertions',
    '-enablesystemassertions',
    '-Dskija.logLevel=DEBUG',
    'org.jetbrains.skija.examples.lwjgl.Main'])

  return 0

if __name__ == '__main__':
  sys.exit(main())