#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

classpath="target/classes:../../shared/target/classes"
xarg_opts=""

add_cp() {
    lib=$1
    repo=${2:-https://repo1.maven.org/maven2}
    if [[ ! -f $HOME/.m2/repository/$lib ]] ; then
        echo "Downloading `basename $lib`"
        mkdir -p $HOME/.m2/repository/`dirname $lib`
        curl --fail --location --show-error --silent -o $HOME/.m2/repository/$lib $repo/$lib
    fi
    classpath=$classpath:$HOME/.m2/repository/$lib
}

add_cp "org/projectlombok/lombok/1.18.12/lombok-1.18.12.jar"
add_cp "org/jetbrains/annotations/19.0.0/annotations-19.0.0.jar"
add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3.jar"
add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3.jar"
add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3.jar"

if [[ `uname` == 'Linux' ]]; then
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-linux.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-linux.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-linux.jar"
    xarg_opts="--no-run-if-empty"
elif [[ `uname` == 'Darwin' ]]; then
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-macos.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-macos.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-macos.jar"
else
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-windows.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-windows.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-windows.jar"
fi

mkdir -p target/classes

if [[ ! -f target/build_timestamp ]]; then
    touch -t 200912310000 target/build_timestamp
fi

find src -name "*.java" -newer target/build_timestamp | xargs $xarg_opts javac -encoding UTF8 --release 11 -cp $classpath -d target/classes

touch target/build_timestamp