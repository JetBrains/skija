#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

rm -rf target/*
rm -rf examples/lwjgl/target/*