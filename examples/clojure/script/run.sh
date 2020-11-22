#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

OS=`uname`
if [[ "$OS" == 'Darwin' ]]; then
   clj -J-XstartOnFirstThread -M -m lwjgl.main
else
    clj -M -m lwjgl.main
fi
