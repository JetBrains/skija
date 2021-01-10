#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

./native/script/build.sh

cd shared

ANNOTATIONS=~/.m2/repository/org/jetbrains/annotations/19.0.0/annotations-19.0.0.jar

if [[ ! -f $ANNOTATIONS ]] ; then
    mkdir -p `dirname $ANNOTATIONS`
    curl --fail --location --show-error https://repo1.maven.org/maven2/org/jetbrains/annotations/19.0.0/annotations-19.0.0.jar > $ANNOTATIONS
fi

LOMBOK=~/.m2/repository/org/projectlombok/lombok/1.18.12/lombok-1.18.12.jar

if [[ ! -f $LOMBOK ]] ; then
    mkdir -p `dirname $LOMBOK`
    curl --fail --location --show-error https://repo1.maven.org/maven2/org/projectlombok/lombok/1.18.12/lombok-1.18.12.jar > $LOMBOK
fi

XARG=""
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
	XARG=--no-run-if-empty
fi

# javac
mkdir -p target

if [ ! -f target/build_timestamp ]; then
    touch -t 200912310000 target/build_timestamp
fi

mkdir -p target/classes/org/jetbrains/skija
find src/main/java/org/jetbrains/skija/lombok.config -newer target/build_timestamp | xargs -I '{}' cp '{}' target/classes/org/jetbrains/skija
find src -name "*.java" -newer target/build_timestamp | xargs $XARG javac -encoding UTF8 --release 11 -cp target/classes:target/test-classes:$ANNOTATIONS:$LOMBOK

# move impl
mkdir -p target/classes/org/jetbrains/skija/impl
find src/main/java/org/jetbrains/skija/impl -name '*.class' | xargs $XARG -I '{}' mv '{}' target/classes/org/jetbrains/skija/impl

# move paragraph
mkdir -p target/classes/org/jetbrains/skija/paragraph
find src/main/java/org/jetbrains/skija/paragraph -name '*.class' | xargs $XARG -I '{}' mv '{}' target/classes/org/jetbrains/skija/paragraph

# move shaper
mkdir -p target/classes/org/jetbrains/skija/shaper
find src/main/java/org/jetbrains/skija/shaper -name '*.class' | xargs $XARG -I '{}' mv '{}' target/classes/org/jetbrains/skija/shaper

# move svg
mkdir -p target/classes/org/jetbrains/skija/svg
find src/main/java/org/jetbrains/skija/svg -name '*.class' | xargs $XARG -I '{}' mv '{}' target/classes/org/jetbrains/skija/svg

# move skottie
mkdir -p target/classes/org/jetbrains/skija/skottie
find src/main/java/org/jetbrains/skija/skottie -name '*.class' | xargs $XARG -I '{}' mv '{}' target/classes/org/jetbrains/skija/skottie

# move skija
find src/main/java -name '*.class' | xargs -I '{}' mv '{}' target/classes/org/jetbrains/skija

# move test/paragraph
mkdir -p target/test-classes/org/jetbrains/skija/paragraph
find src/test/java/org/jetbrains/skija/paragraph -name '*.class' | xargs $XARG -I '{}' mv '{}' target/test-classes/org/jetbrains/skija/paragraph

# move test/test
mkdir -p target/test-classes/org/jetbrains/skija/test
find src/test/java/org/jetbrains/skija/test -name '*.class' | xargs $XARG -I '{}' mv '{}' target/test-classes/org/jetbrains/skija/test

# move test
find src/test/java/org/jetbrains/skija -name '*.class' | xargs $XARG -I '{}' mv '{}' target/test-classes/org/jetbrains/skija

touch target/build_timestamp

# tests
java -cp target/classes:target/test-classes:../native/build org.jetbrains.skija.TestSuite
