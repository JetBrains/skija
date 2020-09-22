#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

REVISION=$(./script/revision.sh)
sed -i "" -e "s/[$][{]revision[}]/$REVISION/g" -e "s/[$][{]platform[}]/macos-x64/g" pom.native.xml
mvn --batch-mode --file pom.native.xml --settings settings.xml -DskipTests -Dspace.username=Nikita.Prokopov -Dspace.password=${SPACE_TOKEN} deploy