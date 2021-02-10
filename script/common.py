#! /usr/bin/env python3

import argparse, os, pathlib, platform, re, shutil, subprocess, sys, time, urllib.request, zipfile

system = {'Darwin': 'macos', 'Linux': 'linux', 'Windows': 'windows'}[platform.system()]
machine = {'AMD64': 'x64', 'x86_64': 'x64', 'arm64': 'arm64'}[platform.machine()]
classpath_separator = ';' if system == 'windows' else ':'
skija_native_artifact_id = 'skija-' + ('macos-' + machine if system == 'macos' else system)
verbose = '--verbose' in sys.argv

def check_call(args, **kwargs):
  t0 = time.time()
  res = subprocess.check_call(args, **kwargs)
  if verbose:
    print('[', round((time.time() - t0) * 1000), 'ms', ']', ' '.join(args))
  return res

def check_output(args, **kwargs):
  kwargs['stdout'] = subprocess.PIPE
  return check_call(args, **kwargs).stdout

def fetch(url, file):
  if not os.path.exists(file):
    print('Downloading', url)
    if os.path.dirname(file):
      os.makedirs(os.path.dirname(file), exist_ok = True)
    # if url.startswith('https://packages.jetbrains.team/'):
    #   check_call(["curl", "--fail", "--location", '--show-error', url, '--output', file])
    # else:
    with open(file, 'wb') as f:
      f.write(urllib.request.urlopen(url).read())

def fetch_maven(group, name, version, classifier=None, repo='https://repo1.maven.org/maven2'):
  path = '/'.join([group.replace('.', '/'), name, version, name + '-' + version + ('-' + classifier if classifier else '') + '.jar'])
  file = os.path.join(os.path.expanduser('~'), '.m2', 'repository', path)
  fetch(repo + '/' + path, file)
  return file

def javac(classpath, sources, target):
  classes = {path.stem: path.stat().st_mtime for path in pathlib.Path(target).rglob('*.class') if '$' not in path.stem}
  newer = lambda path: path.stem not in classes or path.stat().st_mtime > classes.get(path.stem)
  new_sources = [path for path in sources if newer(pathlib.Path(path))]
  if new_sources:
    print('Compiling', len(new_sources), 'java files to', target)
    check_call([
      'javac',
      '-encoding', 'UTF8',
      '--release', '11',
      # '-Xlint:deprecation',
      # '-Xlint:unchecked',
      '--class-path', classpath_separator.join(classpath + [target]),
      '-d', target] + new_sources)

dir_stack = []

def pushd(path):
  dir_stack.append(os.getcwd())
  os.chdir(path)

def popd():
  os.chdir(dir_stack.pop())

def revision():
  desc = check_output(["git", "describe", "--tags", "--match", "*.*.0"], cwd = os.path.dirname(__file__)).decode("utf-8") 
  match = re.match("([0-9]+.[0-9]+).0-([0-9]+)-[a-z0-9]+", desc)
  if match:
    return match.group(1) + "." + match.group(2)
  match = re.match("([0-9]+.[0-9]+).0", desc)
  if match:
    return match.group(1) + ".0"
  raise Exception("Can’t parse revision: " + desc)

def glob(dir, pattern):
  return [str(x) for x in pathlib.Path(dir).rglob(pattern)]