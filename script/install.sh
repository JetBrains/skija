#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

mvn --file pom.shared.xml -DskipTests install
./script/native.sh
mvn --file pom.native.xml -DskipTests install