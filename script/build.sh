#!/usr/bin/env -S sh -eu

cd "`dirname $0`/.."

./script/native.sh

# javac
if [ ! -f "target/build_timestamp" ]; then
    touch -t 200912310000 target/build_timestamp
fi

find src -name "*.java" -newer target/build_timestamp | xargs $JAVA_HOME/bin/javac --release 11 -cp target/classes:target/test-classes

mkdir -p target/classes/org/jetbrains/skija
find src/main/java -name '*.class' | xargs -I '{}' mv '{}' target/classes/org/jetbrains/skija

mkdir -p target/test-classes/org/jetbrains/skija
find src/test/java -name '*.class' | xargs -I '{}' mv '{}' target/test-classes/org/jetbrains/skija

touch target/build_timestamp

# tests
java -cp target/classes:target/test-classes org.jetbrains.skija.TestSuite