rem rmdir /s /q target\native
mkdir target\native
mkdir target\classes

cd target\native

cmake -G Ninja -DSKIA_DIR=%SKIA_DIR% ..\..
ninja
copy %SKIA_DIR%\out\Release-x64\icudtl.dat .
copy *skija.* ..\classes

cd ..\..