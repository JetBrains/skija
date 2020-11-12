#!/bin/bash
set -o errexit -o nounset -o pipefail

if [ -z "${SKIA_DIR-}" ]; then
    echo "Please set \$SKIA_DIR"
    exit 1
fi

skia_dir_abs=$(cd $SKIA_DIR; pwd)
echo "Using Skia from $skia_dir_abs"
build_type=${build_type:-Release}

cd "`dirname $0`/.."

# Build C++
mkdir -p build
pushd build > /dev/null
cmake -G Ninja -DSKIA_DIR=${skia_dir_abs} ..
ninja
popd > /dev/null

# Copy icudtl.dat
[[ -e ${skia_dir_abs}/out/${build_type}-x64/icudtl.dat ]] && cp ${skia_dir_abs}/out/${build_type}-x64/icudtl.dat build

exit 0