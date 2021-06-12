#! /usr/bin/env python3

import argparse, os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common
import script.revision as revision

def main():
  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  rev = revision.revision()
  print('Packaging ' + common.skija_native_artifact_id + ':' + rev)

  with open(os.path.join('build', 'skija.version'), 'w') as f:
    f.write(rev)

  with open('pom.xml', 'r') as f:
    pom = f.read()

  with open('pom.xml', 'w') as f:
    f.write(pom.replace('0.0.0-SNAPSHOT', rev).replace('skija-native', common.skija_native_artifact_id))

  with open(os.path.join('src', 'main', 'java', 'module-info.java'), 'w') as f:
    f.write(pom.replace('skija.windows', common.skija_platform_module_id))

  mvn = 'mvn.cmd' if common.system == 'windows' else 'mvn'
  common.check_call([
    mvn,
    'package'
  ])

  with open('pom.xml', 'w') as f:
    f.write(pom)

  return 0

if __name__ == '__main__':
  sys.exit(main())