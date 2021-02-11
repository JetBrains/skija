#! /usr/bin/env python3

import argparse, os, subprocess, sys
sys.path.append(os.path.normpath(os.path.join(os.path.dirname(__file__), '..', '..')))
import script.common as common
import script.revision as revision

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--dry-run', action='store_true')
  (args, _) = parser.parse_known_args()

  common.pushd(os.path.join(os.path.dirname(__file__), os.pardir))
  rev = revision.revision()
  print('Deploying ' + common.skija_native_artifact_id + ':' + rev)

  with open(os.path.join('build', 'skija.version'), 'w') as f:
    f.write(rev)

  with open('pom.xml', 'r') as f:
    pom = f.read()

  with open('pom.xml', 'w') as f:
    f.write(pom.replace('0.0.0-SNAPSHOT', rev).replace('skija-native', common.skija_native_artifact_id))

  if not args.dry_run:
    mvn = 'mvn.cmd' if common.system == 'windows' else 'mvn'
    common.check_call([
      mvn,
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