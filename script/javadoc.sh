#!/usr/bin/env -S sh -eu

cd "`dirname $0`/.."

javadoc -d docs -sourcepath src/main/java -public org.jetbrains.skija