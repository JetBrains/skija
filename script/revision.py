#! /usr/bin/env python3

import common, os, re, subprocess, sys

def revision():
  os.chdir(common.root)
  desc = subprocess.check_output(["git", "describe", "--tags", "--first-parent", "--match", "*.*.0"], cwd = os.path.dirname(__file__)).decode("utf-8") 
  match = re.match("([0-9]+.[0-9]+).0-([0-9]+)-[a-z0-9]+", desc)
  if match:
    return match.group(1) + "." + match.group(2)
  match = re.match("([0-9]+.[0-9]+).0", desc)
  if match:
    return match.group(1) + ".0"
  raise Exception("Can’t parse revision: " + desc)

if __name__ == '__main__':
  print(revision())
  sys.exit(0)