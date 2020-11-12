rem rmdir /s /q build
mkdir build
mkdir ..\target\classes

rem Build C++
cd build
cmake -G Ninja -DSKIA_DIR=%SKIA_DIR% ..
ninja
cd ..

rem Copy icudtl.dat
copy %SKIA_DIR%\out\Release-x64\icudtl.dat build

rem rem Copy to classes
rem copy build\*skija.* ..\classes