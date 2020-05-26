#!/bin/zsh -euo pipefail
cd `dirname $0`/..

mkdir -p target

if [ ! -f "target/build_timestamp" ]; then
    touch -t 200912310000 target/build_timestamp
fi

function join { local IFS="$1"; shift; echo "$*"; }
CLASSPATH=target/classes:../../target/classes:$(join : `find libs -name "*.jar"`)

mkdir -p target/classes
find src -name "*.java" -newer target/build_timestamp | xargs javac --release 11 -cp $CLASSPATH -d target/classes

touch target/build_timestamp

java -cp $CLASSPATH -XstartOnFirstThread -Djava.awt.headless=true -ea org.jetbrains.skija.examples.lwjgl.Main