#!/bin/bash

$PWD/script/build.sh

echo "******************************"
echo "Do not forget to bump version and avoid using SNAPSHOT!"
echo "******************************"

mvn deploy
