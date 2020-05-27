#!/usr/bin/env -S sh -eu

cd "`dirname $0`/.."

./script/native.sh

mvn test-compile exec:exec install