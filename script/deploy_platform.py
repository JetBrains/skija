#! /usr/bin/env python3
import argparse, build, clean, common, glob, os, platform, revision, subprocess, sys

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument("--dry-run", action="store_true")
  (args, _) = parser.parse_known_args()

  # Build
  build.main()

  # Update poms
  rev = revision.revision()

  os.chdir(common.root + '/platform')
  artifact = "skija-" + common.classifier

  with common.replaced('deploy/META-INF/maven/org.jetbrains.skija/' + artifact + '/pom.xml', {'${version}': rev}):
    with common.replaced('deploy/META-INF/maven/org.jetbrains.skija/' + artifact + '/pom.properties', {'${version}': rev}):
      with open('target/classes/org/jetbrains/skija/' + common.classifier.replace('-', '/') + '/skija.version', 'w') as f:
        f.write(rev)

      print("Packaging " + artifact + "-" + rev + ".jar")
      subprocess.check_call(["jar",
        "--create",
        "--file", "target/" + artifact + "-" + rev + ".jar",
        "-C", "target/classes", ".",
        "-C", "deploy", "META-INF/maven/org.jetbrains.skija/" + artifact
      ])

      if not args.dry_run:
        print("Deploying", artifact + "-" + rev + ".jar")
        subprocess.check_call([
          common.mvn,
          "--batch-mode",
          "--settings", "deploy/settings.xml",
          "-Dspace.username=" + os.getenv("USER_NAME"),
          "-Dspace.password=" + os.getenv("SPACE_TOKEN"),
          "deploy:deploy-file",
          "-Dfile=target/" + artifact + "-" + rev + ".jar",
          "-DpomFile=deploy/META-INF/maven/org.jetbrains.skija/" + artifact + "/pom.xml",
          "-DrepositoryId=space-maven",
          "-Durl=" + common.space_skija,
        ])

  return 0

if __name__ == "__main__":
  sys.exit(main())
