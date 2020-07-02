#!/bin/zsh -euo pipefail

cd "`dirname $0`/.."

./script/native.sh

mvn test-compile exec:exec install