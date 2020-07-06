#!/usr/bin/env -S zsh -euo pipefail

cd `dirname $0`/..

cd third_party/skia
git apply ../skia_skparagraph.patch || true
gn gen out/Release-x64 --args="is_debug=false is_official_build=true skia_use_system_expat=false skia_use_system_icu=false skia_use_system_libjpeg_turbo=false skia_use_system_libpng=false skia_use_system_libwebp=false skia_use_system_zlib=false skia_use_sfntly=false skia_use_freetype=true skia_use_harfbuzz=true skia_pdf_subset_harfbuzz=true skia_use_system_freetype2=false skia_use_system_harfbuzz=false target_cpu=\"x64\" extra_cflags_cc=[\"-frtti\"] cxx=\"g++-9\""
ninja -C out/Release-x64 skia modules

