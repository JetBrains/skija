#!/usr/bin/env -S /bin/zsh -euo pipefail
cd `dirname $0`/..

mkdir -p target

LWJGL_VER=3.2.3
os=`uname`
if [[ "$os" == 'Linux' ]]; then
   LWJGL_OS='linux'
elif [[ "$os" == 'Darwin' ]]; then
   LWJGL_OS='macos'
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


mkdir -p target/classes

find src -name "*.java" | xargs javac --release 11 -cp $CLASSPATH -d target/classes