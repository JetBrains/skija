#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

rm -rf build
rm -rf ~/.m2/repository/org/jetbrains/skija/skija-native/0.0.0-SNAPSHOT