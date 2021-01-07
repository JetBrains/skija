#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

shared=~/.m2/repository/org/jetbrains/skija/skija-shared/0.0.0-SNAPSHOT/skija-shared-0.0.0-SNAPSHOT.jar
native=~/.m2/repository/org/jetbrains/skija/skija-native/0.0.0-SNAPSHOT/skija-native-0.0.0-SNAPSHOT.jar

if [[ ! -f $shared ]] ; then
    ../../shared/script/install.sh
fi

if [[ ! -f $native ]] ; then
    ../../native/script/install.sh
fi

mkdir -p target/classes
javac -d target/classes -encoding UTF8 --release 11 -cp $native:$shared src/*.java
java -cp target/classes:$native:$shared org.jetbrains.skija.examples.bitmap.RenderToBitmap
