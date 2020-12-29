#!/bin/bash
set -o errexit -o nounset -o pipefail

dir=`dirname $0`

if [ -z "${SKIA_DIR-}" ]; then
    . $dir/fetch_skia.sh
fi

skia_dir_abs=$(cd $SKIA_DIR; pwd)
echo "Using Skia from $skia_dir_abs"
build_type=${build_type:-Release}

cd "$dir/.."

# Build C++
mkdir -p build
pushd build > /dev/null
cmake -G Ninja -DCMAKE_BUILD_TYPE=${build_type} --config ${build_type} -DSKIA_DIR=${skia_dir_abs} ..
ninja
popd > /dev/null

# Copy icudtl.dat
[[ -e ${skia_dir_abs}/out/${build_type}-x64/icudtl.dat ]] && cp ${skia_dir_abs}/out/${build_type}-x64/icudtl.dat build

exit 0