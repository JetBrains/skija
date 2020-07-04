#!/usr/bin/env -S /bin/zsh -euo pipefail
cd `dirname $0`/..

mkdir -p target

LWJGL_VER=3.2.3
os=`uname`
JVM_OPTIONS=''
if [[ "$os" == 'Linux' ]]; then
  LWJGL_OS='linux'
elif [[ "$os" == 'Darwin' ]]; then
  LWJGL_OS='macos'
  JVM_OPTIONS='-XstartOnFirstThread'
else
  echo "Unsupported OS $os"
  exit 1
fi

LWJGL_LIBS=(
    lwjgl
    lwjgl-glfw
    lwjgl-opengl
)

HAS_LWJLG=true
CLASSPATH=target/classes:../../target/classes

for LIB in "${LWJGL_LIBS[@]}"; do
    JAR=~/.m2/repository/org/lwjgl/$LIB/$LWJGL_VER/$LIB-$LWJGL_VER.jar
    NATIVE_JAR=~/.m2/repository/org/lwjgl/$LIB/$LWJGL_VER/$LIB-$LWJGL_VER-natives-$LWJGL_OS.jar
    if [[ ! -f $JAR ]]; then
        echo "Missing $JAR"
        HAS_LWJLG=false
    fi
    if [[ ! -f $NATIVE_JAR ]]; then
        echo "Missing $NATIVE_JAR"
        HAS_LWJLG=false
    fi
    CLASSPATH=$CLASSPATH:$JAR:$NATIVE_JAR
done

if [[ "$HAS_LWJLG" == "false" ]]; then
    # fetch missing dependencies
    mvn compile
fi

java -cp $CLASSPATH $JVM_OPTIONS -Djava.awt.headless=true -ea org.jetbrains.skija.examples.lwjgl.Main