#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

REVISION=$(./script/revision.sh)
sed -i "s/[$][{]revision[}]/$REVISION/g" pom.shared.xml
mvn --batch-mode --file pom.shared.xml --settings settings.xml -DskipTests -Dspace.username=Nikita.Prokopov -Dspace.password=${SPACE_TOKEN} deploy