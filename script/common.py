#! /usr/bin/env python3

import argparse, contextlib, os, pathlib, platform, re, shutil, subprocess, sys, time, urllib.request, zipfile

arch = {'AMD64': 'x64', 'x86_64': 'x64', 'arm64': 'arm64'}[platform.machine()]
parser = argparse.ArgumentParser()
parser.add_argument('--arch', default=arch)
(args, _) = parser.parse_known_args()
arch = args.arch

system = {'Darwin': 'macos', 'Linux': 'linux', 'Windows': 'windows'}[platform.system()]
classpath_separator = ';' if system == 'windows' else ':'
mvn = "mvn.cmd" if system == "windows" else "mvn"
space_skija = 'https://packages.jetbrains.team/maven/p/skija/maven'

classifier = ('macos-' + arch if system == 'macos' else system)
module = 'org.jetbrains.skija.' + ('macos.' + arch if system == 'macos' else system)
verbose = '--verbose' in sys.argv
root = os.path.abspath(os.path.dirname(__file__) + '/..')

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

def deps():
  return [
    fetch_maven('org.projectlombok', 'lombok', '1.18.20'),
    fetch_maven('org.jetbrains', 'annotations', '20.1.0'),
  ]

def javac(sources, target, classpath = [], modulepath = [], add_modules = [], release = '11', opts = []):
  classes = {path.stem: path.stat().st_mtime for path in pathlib.Path(target).rglob('*.class') if '$' not in path.stem}
  newer = lambda path: path.stem not in classes or path.stat().st_mtime > classes.get(path.stem)
  new_sources = [path for path in sources if newer(pathlib.Path(path))]
  if new_sources:
    print('Compiling', len(new_sources), 'java files to', target)
    check_call([
      'javac',
      '-encoding', 'UTF8',
      '--release', release] + opts + [
      # '-J--illegal-access=permit',
      # '-Xlint:deprecation',
      # '-Xlint:unchecked',
      '--class-path', classpath_separator.join(classpath + [target])] +
      (['--module-path', classpath_separator.join(modulepath)] if modulepath else []) +
      (['--add-modules', ','.join(add_modules)] if add_modules else []) +
      ['-d', target] + new_sources)

def glob(dir, pattern):
  return [str(x) for x in pathlib.Path(dir).rglob(pattern)]

@contextlib.contextmanager
def replaced(filename, replacements):
    with open(filename, 'r') as f:
      original = f.read()
    try:  
      updated = original
      for key, value in replacements.items():
        updated = updated.replace(key, value)
      with open(filename, 'w') as f:
        f.write(updated)
      yield f
    finally:
      with open(filename, 'w') as f:
        f.write(original)
        
def copy_newer(src, dst):
  if not os.path.exists(dst) or os.path.getmtime(src) > os.path.getmtime(dst):
    if os.path.exists(dst):
      os.remove(dst)
    shutil.copy2(src, dst)