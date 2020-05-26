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
if [ -f "target/classes/libskija.dylib" ]; then
    find target/native/libskija.dylib -newer target/classes/libskija.dylib | xargs -I '{}' cp '{}' target/classes
else
    cp target/native/libskija.dylib target/classes
fi
