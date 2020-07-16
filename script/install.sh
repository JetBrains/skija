#!/usr/bin/env -S zsh -euo pipefail

cd "`dirname $0`/.."

./script/native.sh

mvn verify exec:exec

pushd publish
./gradlew publishToMavenLocal
popd