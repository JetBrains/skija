#!/bin/zsh -euo pipefail

cd "`dirname $0`/.."

./script/native.sh

mvn -Dmaven.test.skip=true test-compile exec:exec install