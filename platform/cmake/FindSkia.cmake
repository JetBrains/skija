# https://github.com/aseprite/laf/blob/29d275d47087acb897f3d4791a055963934906a1/cmake/FindSkia.cmake

# Copyright (C) 2019-2020  Igara Studio S.A.
#
# This file is released under the terms of the MIT license.
# Read LICENSE.txt for more information.

set(SKIA_DIR "" CACHE PATH "Skia source code directory")
if(NOT SKIA_DIR)
  set(SKIA_LIBRARY_DIR "" CACHE PATH "Skia library directory (where libskia.a is located)")
else()
  if(CMAKE_BUILD_TYPE STREQUAL Debug)
    set(SKIA_LIBRARY_DIR "${SKIA_DIR}/out/Debug-${SKIA_ARCH}" CACHE PATH "Skia library directory" FORCE)
  else()
    set(SKIA_LIBRARY_DIR "${SKIA_DIR}/out/Release-${SKIA_ARCH}" CACHE PATH "Skia library directory" FORCE)
  endif()
endif()

# Skia library
find_library(SKIA_LIBRARY skia PATH "${SKIA_LIBRARY_DIR}")
if(WIN32)
  set(SKIA_OPENGL_LIBRARY opengl32 CACHE STRING "...")
elseif(APPLE)
  find_library(SKIA_OPENGL_LIBRARY OpenGL NAMES GL)
else()
  find_library(SKIA_OPENGL_LIBRARY opengl NAMES GL)
endif()

# SkShaper module + freetype + harfbuzz
find_library(SKSHAPER_LIBRARY skshaper PATH "${SKIA_LIBRARY_DIR}")
find_path(SKSHAPER_INCLUDE_DIR SkShaper.h HINTS "${SKIA_DIR}/modules/skshaper/include")
if(NOT FREETYPE_LIBRARIES)
  set(FREETYPE_FOUND ON)
  if (UNIX AND NOT APPLE)
    # Dynamically linked because fontconfig is dynamically linked
    # https://github.com/JetBrains/skija/issues/113
    find_library(FREETYPE_LIBRARY freetype)
  else()
    find_library(FREETYPE_LIBRARY freetype2 PATH "${SKIA_LIBRARY_DIR}")
  endif()
  set(FREETYPE_LIBRARIES ${FREETYPE_LIBRARY})
  set(FREETYPE_INCLUDE_DIRS "${SKIA_DIR}/third_party/externals/freetype/include")
endif()
if(NOT HARFBUZZ_LIBRARIES)
  find_library(HARFBUZZ_LIBRARY NAMES harfbuzz HINTS "${SKIA_LIBRARY_DIR}")
  set(HARFBUZZ_LIBRARIES ${HARFBUZZ_LIBRARY})
  set(HARFBUZZ_INCLUDE_DIRS "${SKIA_DIR}/third_party/externals/harfbuzz/src")
endif()
add_library(skshaper INTERFACE)
target_link_libraries(skshaper INTERFACE ${SKSHAPER_LIBRARY})

# SkParagraph
find_library(SKPARAGRAPH_LIBRARY skparagraph PATH "${SKIA_LIBRARY_DIR}")
find_path(SKPARAGRAPH_INCLUDE_DIR Paragraph.h HINTS "${SKIA_DIR}/modules/skparagraph/include")
add_library(skparagraph INTERFACE)
target_link_libraries(skparagraph INTERFACE ${SKPARAGRAPH_LIBRARY})

# SVG
find_library(SKIA_SVG_LIBRARY svg PATH "${SKIA_LIBRARY_DIR}")
add_library(svg INTERFACE)
target_link_libraries(svg INTERFACE ${SKIA_SVG_LIBRARY})
find_path(SKIA_SVG_INCLUDE_DIR SkSVGDOM.h HINTS "${SKIA_DIR}/modules/svg/include")

# SkResources
find_library(SKIA_SKRESOURCES_LIBRARY skresources PATH "${SKIA_LIBRARY_DIR}")
add_library(skresources INTERFACE)
target_link_libraries(skresources INTERFACE ${SKIA_SKRESOURCES_LIBRARY})
find_path(SKIA_SKRESOURCES_INCLUDE_DIR SkResources.h HINTS "${SKIA_DIR}/modules/skresources/include")

# SKOTTIE
find_library(SKIA_SKOTTIE_LIBRARY skottie PATH "${SKIA_LIBRARY_DIR}")
add_library(skottie INTERFACE)
target_link_libraries(skottie INTERFACE ${SKIA_SKOTTIE_LIBRARY})
find_path(SKIA_SKOTTIE_INCLUDE_DIR Skottie.h HINTS "${SKIA_DIR}/modules/skottie/include")

# SKSG
find_library(SKIA_SKSG_LIBRARY sksg PATH "${SKIA_LIBRARY_DIR}")
add_library(sksg INTERFACE)
target_link_libraries(sksg INTERFACE ${SKIA_SKSG_LIBRARY})
find_path(SKIA_SKSG_INCLUDE_DIR SkSGInvalidationController.h HINTS "${SKIA_DIR}/modules/sksg/include")

find_path(SKIA_CONFIG_INCLUDE_DIR SkUserConfig.h HINTS "${SKIA_DIR}/include/config")
find_path(SKIA_CORE_INCLUDE_DIR SkCanvas.h HINTS "${SKIA_DIR}/include/core")
find_path(SKIA_PATHOPS_INCLUDE_DIR SkPathOps.h HINTS "${SKIA_DIR}/include/pathops")
find_path(SKIA_CORE_SVG_INCLUDE_DIR SkSVGCanvas.h HINTS "${SKIA_DIR}/include/svg")
find_path(SKIA_UTILS_INCLUDE_DIR SkRandom.h HINTS "${SKIA_DIR}/include/utils")
find_path(SKIA_CODEC_INCLUDE_DIR SkCodec.h HINTS "${SKIA_DIR}/include/codec")
find_path(SKIA_EFFECTS_INCLUDE_DIR SkShaderMaskFilter.h HINTS "${SKIA_DIR}/include/effects")
find_path(SKIA_GPU_INCLUDE_DIR GrDirectContext.h HINTS "${SKIA_DIR}/include/gpu")
find_path(SKIA_GPU2_INCLUDE_DIR gl/GrGLDefines.h HINTS "${SKIA_DIR}/src/gpu")
find_path(SKIA_ANGLE_INCLUDE_DIR angle_gl.h HINTS "${SKIA_DIR}/third_party/externals/angle2/include")
find_path(SKIA_SKCMS_INCLUDE_DIR skcms.h
  HINTS
  "${SKIA_DIR}/third_party/skcms"
  "${SKIA_DIR}/include/third_party/skcms")

set(SKIA_LIBRARIES
  ${SKIA_LIBRARY}
  ${SKIA_OPENGL_LIBRARY}
  CACHE INTERNAL "Skia libraries")

add_library(skia INTERFACE)
target_include_directories(skia INTERFACE
  ${SKIA_DIR}
  ${SKIA_CONFIG_INCLUDE_DIR}
  ${SKIA_CORE_INCLUDE_DIR}
  ${SKIA_PATHOPS_INCLUDE_DIR}
  ${SKIA_CORE_SVG_INCLUDE_DIR}
  ${SKIA_UTILS_INCLUDE_DIR}
  ${SKIA_CODEC_INCLUDE_DIR}
  ${SKIA_EFFECTS_INCLUDE_DIR}
  ${SKIA_GPU_INCLUDE_DIR}
  ${SKIA_GPU2_INCLUDE_DIR}
  ${SKIA_SKCMS_INCLUDE_DIR}
  ${SKSHAPER_INCLUDE_DIR}
  ${SKPARAGRAPH_INCLUDE_DIR}
  ${FREETYPE_INCLUDE_DIRS}
  ${HARFBUZZ_INCLUDE_DIRS}
  ${SKIA_SVG_INCLUDE_DIR}
  ${SKIA_SKOTTIE_INCLUDE_DIR}
  ${SKIA_SKSG_INCLUDE_DIR}
  ${SKIA_DIR}/third_party/externals/icu/source/common
  ${SKIA_DIR}/third_party/icu
  ${SKIA_SKRESOURCES_INCLUDE_DIR}
)

if(WIN32)
  target_include_directories(skia INTERFACE
    ${SKIA_ANGLE_INCLUDE_DIR})
endif()
target_link_libraries(skia INTERFACE ${SKIA_LIBRARIES})
target_compile_definitions(skia INTERFACE
  SK_INTERNAL
  SK_GAMMA_SRGB
  SK_GAMMA_APPLY_TO_A8
  SK_SCALAR_TO_FLOAT_EXCLUDED
  SK_ALLOW_STATIC_GLOBAL_INITIALIZERS=1
  SK_SUPPORT_OPENCL=0
  SK_FORCE_DISTANCE_FIELD_TEXT=0
  SK_SUPPORT_GPU=1)

if(WIN32)
  target_compile_definitions(skia INTERFACE
    SK_BUILD_FOR_WIN32
    _CRT_SECURE_NO_WARNINGS
    GR_GL_FUNCTION_TYPE=__stdcall)
elseif(APPLE)
  target_compile_definitions(skia INTERFACE
    SK_BUILD_FOR_MAC)
else()
  target_compile_definitions(skia INTERFACE
    SK_SAMPLES_FOR_X)
endif()

if(APPLE)
  find_library(COCOA_LIBRARY Cocoa)
  find_library(METAL_LIBRARY Metal)
  target_link_libraries(skia INTERFACE
    ${COCOA_LIBRARY} ${METAL_LIBRARY})
endif()

if(UNIX AND NOT APPLE)
  # Change the kN32_SkColorType ordering to BGRA to work in X windows.
  target_compile_definitions(skia INTERFACE
    SK_R32_SHIFT=16)

  # Needed for SkFontMgr on Linux
  find_library(FONTCONFIG_LIBRARY fontconfig)
  target_link_libraries(skia INTERFACE
    ${FONTCONFIG_LIBRARY})
endif()

