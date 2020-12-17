#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

OS=`uname`
if [[ "$OS" == 'Darwin' ]]; then
   clj -J-XstartOnFirstThread -M -m snake.main
else
    clj -M -m snake.main
fi
