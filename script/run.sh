#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

./examples/lwjgl/script/build.sh && ./examples/lwjgl/script/run.sh