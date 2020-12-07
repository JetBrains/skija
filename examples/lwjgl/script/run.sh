#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

classpath="target/classes:../../native/build:../../shared/target/classes"
java_opts=""

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

add_cp "org/jetbrains/annotations/19.0.0/annotations-19.0.0.jar"
add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3.jar"
add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3.jar"
add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3.jar"

if [[ `uname` == 'Linux' ]]; then
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-linux.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-linux.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-linux.jar"
elif [[ `uname` == 'Darwin' ]]; then
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-macos.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-macos.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-macos.jar"
    java_opts="-XstartOnFirstThread"
else
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-windows.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-windows.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-windows.jar"
fi

java -cp $classpath $java_opts -Djava.awt.headless=true -ea org.jetbrains.skija.examples.lwjgl.Main