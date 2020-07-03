#!/usr/bin/env -S /bin/zsh -euo pipefail

cd "`dirname $0`/.."

setopt BASH_REMATCH
COORD=`git describe --tags --match "*.*.0"`
if [[ $COORD =~ '([0-9]+.[0-9]+).0-([0-9]+)-[a-z0-9]+' ]] ; then
    echo "${BASH_REMATCH[2]}.${BASH_REMATCH[3]}"
elif [[ $COORD =~ '([0-9]+.[0-9]+).0' ]] ; then
    echo "${BASH_REMATCH[2]}.0"
fi