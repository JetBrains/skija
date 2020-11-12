#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

mvn lombok:delombok javadoc:javadoc