#! /usr/bin/env python3

import argparse, os, platform, re, shutil, subprocess, sys, urllib.request, zipfile

system = {'Darwin': 'macos', 'Linux': 'linux', 'Windows': 'windows'}[platform.system()]
machine = {'AMD64': 'x64', 'x86_64': 'x64', 'arm64': 'arm64'}[platform.machine()]
classpath_separator = ';' if system == 'windows' else ':'
skija_native_artifact_id = 'skija-' + ('macos-' + machine if system == 'macos' else system)

def fetch(url, file):
  if not os.path.exists(file):
    print('Downloading', url)
    if os.path.dirname(file):
      os.makedirs(os.path.dirname(file), exist_ok = True)
    # if url.startswith('https://packages.jetbrains.team/'):
    #   subprocess.check_call(["curl", "--fail", "--location", '--show-error', url, '--output', file])
    # else:
    with open(file, 'wb') as f:
      f.write(urllib.request.urlopen(url).read())

def fetch_maven(group, name, version, classifier=None, repo='https://repo1.maven.org/maven2'):
  path = '/'.join([group.replace('.', '/'), name, version, name + '-' + version + ('-' + classifier if classifier else '') + '.jar'])
  file = os.path.join(os.path.expanduser('~'), '.m2', 'repository', path)
  fetch(repo + '/' + path, file)
  return file

def javac(classpath, sources, target):
  subprocess.check_call([
    'javac',
    '-encoding', 'UTF8',
    '--release', '11',
    # '-Xlint:deprecation',
    # '-Xlint:unchecked',
    '--class-path', classpath_separator.join(classpath),
    '-d', target] + sources)

dir_stack = []

def pushd(path):
  dir_stack.append(os.getcwd())
  os.chdir(path)

def popd():
  os.chdir(dir_stack.pop())

def revision():
  desc = subprocess.check_output(["git", "describe", "--tags", "--match", "*.*.0"], cwd = os.path.dirname(__file__)).decode("utf-8") 
  match = re.match("([0-9]+.[0-9]+).0-([0-9]+)-[a-z0-9]+", desc)
  if match:
    return match.group(1) + "." + match.group(2)
  match = re.match("([0-9]+.[0-9]+).0", desc)
  if match:
    return match.group(1) + ".0"
  raise Exception("Can’t parse revision: " + desc)
