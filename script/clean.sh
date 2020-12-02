#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

./shared/script/clean.sh
./native/script/clean.sh
./examples/bitmap/script/clean.sh
./examples/clojure/script/clean.sh
./examples/jogl/script/clean.sh
./examples/lwjgl/script/clean.sh
