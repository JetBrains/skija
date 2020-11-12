#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

./shared/script/install.sh
./native/script/build.sh
./native/script/install.sh
