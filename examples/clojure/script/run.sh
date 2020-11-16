#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

clj -J-XstartOnFirstThread -X lwjgl.main/-main