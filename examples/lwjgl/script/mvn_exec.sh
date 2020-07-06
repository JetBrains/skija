#!/usr/bin/env -S zsh -euo pipefail
cd `dirname $0`/..

mvn compile exec:exec
