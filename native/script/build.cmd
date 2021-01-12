rem rmdir /s /q build
mkdir build
mkdir ..\target\classes

rem Build C++
cd build
cmake -G Ninja -DCMAKE_BUILD_TYPE=Release -DSKIA_DIR=..\Skia-m88-21ebdec517-windows-Release-x64 ..
rem cmake -G Ninja -DCMAKE_BUILD_TYPE=Release -DSKIA_DIR=\Users\proko\ws\skia-build\skia ..
ninja
cd ..

rem Copy icudtl.dat
copy Skia-m88-21ebdec517-windows-Release-x64\out\Release-x64\icudtl.dat build
rem copy \Users\proko\ws\skia-build\skia\out\Release-x64\icudtl.dat build

rem rem Copy to classes
rem copy build\*skija.* ..\classes