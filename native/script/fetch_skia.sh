#!/bin/bash
set -o errexit -o nounset -o pipefail

skia_release=m88-fc6759b235

OS=`uname`
if [[ "$OS" == 'Linux' ]]; then
    platform='linux'
elif [[ "$OS" == 'Darwin' ]]; then
    platform='macos'
else
    platform='windows'
fi

pushd `dirname $0`/.. > /dev/null

SKIA_DIR=Skia-${skia_release}-${platform}-Release-x64
if [ ! -d $SKIA_DIR ] ; then
	echo "Downloading ${SKIA_DIR}.zip"
	curl --fail --location --silent --show-error  https://github.com/JetBrains/skia-build/releases/download/${skia_release}/${SKIA_DIR}.zip > ${SKIA_DIR}.zip
	unzip -qq ${SKIA_DIR}.zip -d $SKIA_DIR
	rm ${SKIA_DIR}.zip
fi
SKIA_DIR=`pwd`/${SKIA_DIR}

popd > /dev/null