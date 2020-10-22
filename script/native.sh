#!/bin/bash
set -o errexit -o nounset -o pipefail

SKIA_DIR=${SKIA_DIR:-`dirname $0`/../third_party/skia}
SKIA_DIR_ABS=$(cd $SKIA_DIR; pwd)
echo "Using Skia from $SKIA_DIR_ABS"

cd "`dirname $0`/.."

# Build C++
mkdir -p target/native
pushd target/native > /dev/null
cmake -G Ninja -DSKIA_DIR=$SKIA_DIR_ABS ../..
ninja
popd > /dev/null

mkdir -p target/classes
cp target/native/*skija.* target/classes
