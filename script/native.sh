#!/usr/bin/env -S sh -eu

SKIA_DIR=${SKIA_DIR:-`dirname $0`/../third_party/skia}
SKIA_DIR_ABS=$(cd $SKIA_DIR; pwd)

cd "`dirname $0`/.."

# Build C++
mkdir -p target/native
SAVED="`pwd`"
cd target/native
cmake -G Ninja -DSKIA_DIR=$SKIA_DIR_ABS ../..
ninja
cd "$SAVED"

LIB_EXTENSION=""
case "$(uname -s)" in

   Darwin)
     LIB_EXTENSION="dylib"
     ;;

   Linux)
     LIB_EXTENSION="so"
     ;;

   CYGWIN*|MINGW32*|MSYS*|MINGW*)
     LIB_EXTENSION="dll"
     ;;

   *)
     echo 'Unsupported OS. Please update 'native.sh' script'
     ;;
esac

mkdir -p target/classes
if [ -f "target/classes/libskija.$LIB_EXTENSION" ]; then
    find target/native/libskija.$LIB_EXTENSION -newer target/classes/libskija.$LIB_EXTENSION | xargs -I '{}' cp '{}' target/classes
else
    cp target/native/libskija.$LIB_EXTENSION target/classes
fi