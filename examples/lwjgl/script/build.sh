#!/usr/bin/env -S zsh -euo pipefail
cd `dirname $0`/..

mkdir -p target

LWJGL_VER=3.2.3
OS=`uname`
if [[ "$OS" == 'Linux' ]]; then
   LWJGL_QUALIFIER='linux'
elif [[ "$OS" == 'Darwin' ]]; then
   LWJGL_QUALIFIER='macos'
else
  echo "Unsupported OS, expected: Linux | Darwin, got: $OS"
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
    NATIVE_JAR=~/.m2/repository/org/lwjgl/$LIB/$LWJGL_VER/$LIB-$LWJGL_VER-natives-$LWJGL_QUALIFIER.jar
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

if [[ ! -f target/build_timestamp ]]; then
    touch -t 200912310000 target/build_timestamp
fi

find src -name "*.java" -newer target/build_timestamp | xargs javac --release 11 -cp $CLASSPATH -d target/classes

touch target/build_timestamp