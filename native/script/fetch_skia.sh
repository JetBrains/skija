#!/bin/bash
set -o errexit -o nounset -o pipefail

skia_release=m89-15595ea39c
build_type=${build_type:-Release}

OS=`uname`
arch='x64'
if [[ "$OS" == 'Linux' ]]; then
    platform='linux'
elif [[ "$OS" == 'Darwin' ]]; then
    platform='macos'
    if [ $(uname -m) == "arm64" ]; then
        arch='arm64'
    fi
else
    platform='windows'
fi

pushd `dirname $0`/.. > /dev/null

SKIA_DIR=Skia-${skia_release}-${platform}-${build_type}-${arch}
if [ ! -d $SKIA_DIR ] ; then
	echo "Downloading ${SKIA_DIR}.zip"
	curl --fail --location --silent --show-error  https://github.com/JetBrains/skia-build/releases/download/${skia_release}/${SKIA_DIR}.zip > ${SKIA_DIR}.zip
	unzip -qq ${SKIA_DIR}.zip -d $SKIA_DIR
	rm ${SKIA_DIR}.zip
fi
SKIA_DIR=`pwd`/${SKIA_DIR}

popd > /dev/null