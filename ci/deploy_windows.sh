#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

REVISION=$(./ci/revision.sh)
echo "Deploying skija-windows v${REVISION}"
sed -i -e "s/0.0.0-SNAPSHOT/$REVISION/g" -e "s/platform/windows/g" pom.native.xml
mvn --batch-mode --file pom.native.xml --settings settings.xml -DskipTests -Dspace.username=Nikita.Prokopov -Dspace.password=${SPACE_TOKEN} deploy