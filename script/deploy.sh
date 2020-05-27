#!/usr/bin/env -S sh -eu
cd "`dirname $0`/.."
REVISION=`./script/revision.sh`
mvn -Drevision=$REVISION deploy
