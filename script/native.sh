#!/bin/zsh -euo pipefail

SKIA_DIR=${SKIA_DIR:-`dirname $0`/../third_party/skia}
SKIA_DIR_ABS=$(cd $SKIA_DIR; pwd)

cd "`dirname $0`/.."

# Build C++
mkdir -p target/native
pushd target/native
cmake -G Ninja -DSKIA_DIR=$SKIA_DIR_ABS ../..
ninja
popd

mkdir -p target/classes
cp target/native/libskija.* target/classes