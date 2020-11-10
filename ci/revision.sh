#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

COORD=`git describe --tags --match "*.*.0"`
if [[ $COORD =~ ([0-9]+.[0-9]+).0-([0-9]+)-[a-z0-9]+ ]] ; then
    echo ${BASH_REMATCH[1]}.${BASH_REMATCH[2]}
elif [[ $COORD =~ ([0-9]+.[0-9]+).0 ]] ; then
    echo ${BASH_REMATCH[1]}.0
fi