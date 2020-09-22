#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

REVISION=$(./script/revision.sh)
mvn --batch-mode --file pom.shared.xml versions:set -DnewVersion=${REVISION}-shared
mvn --batch-mode --file pom.shared.xml --settings settings.xml -DskipTests -Dspace.username=Nikita.Prokopov -Dspace.password=${SPACE_TOKEN} deploy