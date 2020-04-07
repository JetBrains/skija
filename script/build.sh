#!/bin/zsh -xeuo pipefail

SKIA_DIR=${SKIA_DIR:-`dirname $0`/../third_party/skia}
SKIA_DIR_ABS=$(cd $SKIA_DIR; pwd)

cd "`dirname $0`/.."

mkdir -p target/native
pushd target/native
cmake -G Ninja -DSKIA_DIR=$SKIA_DIR_ABS ../..
ninja
popd

mvn compile install