#!/usr/bin/env -S /bin/zsh -euo pipefail
cd `dirname $0`/..

mvn compile exec:exec
