#!/bin/zsh -euo pipefail
cd "`dirname $0`/.."
REVISION=`./script/revision.sh`
mvn -Drevision=$REVISION deploy
