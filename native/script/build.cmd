rem rmdir /s /q build
mkdir build
mkdir ..\target\classes

rem Build C++
cd build
cmake -G Ninja -DSKIA_DIR=..\Skia-m88-b05f80697a-windows-Release-x64 ..
ninja
cd ..

rem Copy icudtl.dat
copy Skia-m88-b05f80697a-windows-Release-x64\out\Release-x64\icudtl.dat build

rem rem Copy to classes
rem copy build\*skija.* ..\classes