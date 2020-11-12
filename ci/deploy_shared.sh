#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

REVISION=$(./ci/revision.sh)
echo "Deploying skija-shared v${REVISION}"
cd shared
sed -i "s/0.0.0-SNAPSHOT/$REVISION/g" pom.xml

mvn --batch-mode -DskipTests lombok:delombok javadoc:javadoc javadoc:jar source:jar
mvn --batch-mode --settings ../ci/settings.xml -DskipTests -Dspace.username=Nikita.Prokopov -Dspace.password=${SPACE_TOKEN} deploy
