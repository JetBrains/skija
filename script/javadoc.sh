#!/bin/zsh -euo pipefail

cd "`dirname $0`/.."

javadoc -d docs/api -sourcepath src/main/java -public org.jetbrains.skija