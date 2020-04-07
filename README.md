# Skija: Java bindings for Skia

![](extras/logo.png)

## Checkout

```sh
git clone ssh://git@git.jetbrains.team/Skija.git
cd skija
```

## Building Skia

Install `depot_tools` somewhere:

```sh
git clone 'https://chromium.googlesource.com/chromium/tools/depot_tools.git'
export PATH="${PWD}/depot_tools:${PATH}"
```

Check out `skia` submodule:

```sh
cd third_party/skia
git submodule update --init
```

Build Skia:

```sh
python2 tools/git-sync-deps
gn gen out/Release-x64 --args="is_debug=false is_official_build=true skia_use_system_expat=false skia_use_system_icu=false skia_use_system_libjpeg_turbo=false skia_use_system_libpng=false skia_use_system_libwebp=false skia_use_system_zlib=false skia_use_sfntly=false skia_use_freetype=true skia_use_harfbuzz=true skia_pdf_subset_harfbuzz=true skia_use_system_freetype2=false skia_use_system_harfbuzz=false target_cpu=\"x64\" extra_cflags=[\"-stdlib=libc++\", \"-mmacosx-version-min=10.9\"] extra_cflags_cc=[\"-frtti\"]"
ninja -C out/Release-x64 skia modules
```

## Using prebuilt Skia

Download & unpack from e.g. [github.com/aseprite/skia/releases](https://github.com/aseprite/skia/releases).

```sh
export SKIA_DIR=~/Downloads/Skia-macOS-Release-x64
```

## Building Skija

Prerequisites:

- cmake
- ninja
- JDK 11+ and JAVA_HOME
- maven

```sh
./script/build.sh
```

## Running examples

```sh
./script/run.sh
```