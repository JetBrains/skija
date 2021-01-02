#!/bin/bash
set -o errexit -o nounset -o pipefail -o xtrace
dir=`dirname $0`

docker build --tag skija .

[ "$(docker ps -a | grep dummy)" ] && docker rm --force dummy
id=`docker create --tty --interactive --name dummy skija bash`

echo "Installing ~/.m2/repository/org/jetbrains/skija/skija-native/0.0.0-SNAPSHOT/skija-native-0.0.0-SNAPSHOT.jar"
mkdir --parents ~/.m2/repository/org/jetbrains/skija/skija-native/
docker cp $id:/root/.m2/repository/org/jetbrains/skija/skija-native/0.0.0-SNAPSHOT ~/.m2/repository/org/jetbrains/skija/skija-native/

echo "Installing ~/.m2/repository/org/jetbrains/skija/skija-shared/0.0.0-SNAPSHOT/skija-shared-0.0.0-SNAPSHOT.jar"
mkdir --parents ~/.m2/repository/org/jetbrains/skija/skija-shared/
docker cp $id:/root/.m2/repository/org/jetbrains/skija/skija-shared/0.0.0-SNAPSHOT ~/.m2/repository/org/jetbrains/skija/skija-shared/

docker rm --force dummy