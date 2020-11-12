#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

rm -rf docs/apidocs
rm -rf target
rm -rf ~/.m2/repository/org/jetbrains/skija/skija-shared/0.0.0-SNAPSHOT