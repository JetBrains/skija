#! /usr/bin/env python3

import os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common
import script.revision as revision

def main():
  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  rev = revision.revision()
  print('Deploying ' + common.skija_native_artifact_id + ':' + rev)

  with open('pom.xml', 'r') as f:
    pom = f.read()

  with open('pom.xml', 'w') as f:
    f.write(pom.replace('0.0.0-SNAPSHOT', rev).replace('skija-native', common.skija_native_artifact_id))

  common.check_call([
    'mvn.cmd' if common.system == 'windows' else 'mvn',
    '--batch-mode',
    '--settings', 'settings.xml',
    '-Dspace.username=Nikita.Prokopov',
    '-Dspace.password=' + os.getenv('SPACE_TOKEN'),
    'deploy'
  ])

  with open('pom.xml', 'w') as f:
    f.write(pom)

  return 0

if __name__ == '__main__':
  sys.exit(main())