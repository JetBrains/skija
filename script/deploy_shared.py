#! /usr/bin/env python3
import argparse, build_shared, clean, common, glob, os, platform, revision, subprocess, sys

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument('--dry-run', action='store_true')
  (args, _) = parser.parse_known_args()

  # Build
  build_shared.main()

  # Update poms
  rev = revision.revision()

  os.chdir(common.root + '/shared')

  with common.replaced('deploy/META-INF/maven/org.jetbrains.skija/skija-shared/pom.xml', {'${version}': rev}):
    with common.replaced('deploy/META-INF/maven/org.jetbrains.skija/skija-shared/pom.properties', {'${version}': rev}):

      # skija-shared-*.jar
      print('Packaging skija-shared-' + rev + ".jar")
      subprocess.check_call(["jar",
        "--create",
        "--file", "target/skija-shared-" + rev + ".jar",
        "-C", "target/classes", ".",
        "-C", "deploy", "META-INF"
      ])

      if not args.dry_run:
        print('Deploying skija-shared-' + rev + ".jar")
        subprocess.check_call([
          common.mvn,
          '--batch-mode',
          '--settings', 'deploy/settings.xml',
          '-Dspace.username'  + os.getenv("USER_NAME"),
          '-Dspace.password=' + os.getenv('SPACE_TOKEN'),
          'deploy:deploy-file',
          "-Dfile=target/skija-shared-" + rev + ".jar",
          "-DpomFile=deploy/META-INF/maven/org.jetbrains.skija/skija-shared/pom.xml",
          "-DrepositoryId=space-maven",
          "-Durl=" + common.space_skija,
        ])

      # skija-shared-*-sources.jar
      lombok = common.deps()[0]
      print('Delomboking sources')
      subprocess.check_call([
        "java",
        "-Dfile.encoding=UTF8",
        "-jar",
        lombok,
        "delombok",
        "java",
        "--module-path",
        common.classpath_separator.join(common.deps()),
        "-d", "target/generated-sources/delombok/org/jetbrains/skija"
      ])

      print('Packaging skija-shared-' + rev + "-sources.jar")
      subprocess.check_call(["jar",
        "--create",
        "--file", "target/skija-shared-" + rev + "-sources.jar",
        "-C", "target/generated-sources/delombok", ".",
        "-C", "deploy", "META-INF"
      ])

      if not args.dry_run:
        print('Deploying skija-shared-' + rev + "-sources.jar")
        subprocess.check_call([
          common.mvn,
          '--batch-mode',
          '--settings', 'deploy/settings.xml',
          '-Dspace.username=' + os.getenv("USER_NAME"),
          '-Dspace.password=' + os.getenv('SPACE_TOKEN'),
          'deploy:deploy-file',
          "-Dpackaging=java-source",
          "-Dfile=target/skija-shared-" + rev + "-sources.jar",
          "-DpomFile=deploy/META-INF/maven/org.jetbrains.skija/skija-shared/pom.xml",
          "-DrepositoryId=space-maven",
          "-Durl=" + common.space_skija,
        ])

  return 0

if __name__ == '__main__':
  sys.exit(main())
