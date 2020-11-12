#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

./shared/script/clean.sh
./native/script/clean.sh
./examples/lwjgl/script/clean.sh
