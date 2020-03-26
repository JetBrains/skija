#!/bin/zsh -euo pipefail
cd `dirname $0`/../examples/lwjgl

mvn compile exec:exec