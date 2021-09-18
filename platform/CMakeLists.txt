cmake_minimum_required(VERSION 3.9)
project(skija LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

if(NOT SKIA_ARCH)
    if("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "x86_64" OR "${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "AMD64")
        set(SKIA_ARCH "x64")
    elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "arm64")
        set(SKIA_ARCH "arm64")
    else()
        set(SKIA_ARCH "x86")
    endif()
endif()

# Sources

file(GLOB SOURCES ${CMAKE_SOURCE_DIR}/cc/*.cc)
file(GLOB IMPL_SOURCES ${CMAKE_SOURCE_DIR}/cc/impl/*.cc)
file(GLOB PARAGRAPH_SOURCES ${CMAKE_SOURCE_DIR}/cc/paragraph/*.cc)
file(GLOB SHAPER_SOURCES ${CMAKE_SOURCE_DIR}/cc/shaper/*.cc)
file(GLOB SVG_SOURCES ${CMAKE_SOURCE_DIR}/cc/svg/*.cc)
file(GLOB RESOURCES_SOURCES ${CMAKE_SOURCE_DIR}/cc/resources/*.cc)
file(GLOB SKOTTIE_SOURCES ${CMAKE_SOURCE_DIR}/cc/skottie/*.cc)
file(GLOB SKSG_SOURCES ${CMAKE_SOURCE_DIR}/cc/sksg/*.cc)
add_library(skija SHARED ${SOURCES} ${IMPL_SOURCES} ${PARAGRAPH_SOURCES} ${SHAPER_SOURCES} ${SVG_SOURCES} ${RESOURCES_SOURCES} ${SKOTTIE_SOURCES} ${SKSG_SOURCES})

add_definitions(-DFT2_BUILD_LIBRARY
    -DFT_CONFIG_MODULES_H=<include/freetype-android/ftmodule.h>
    -DFT_CONFIG_OPTIONS_H=<include/freetype-android/ftoption.h>
    -DPNG_INTEL_SSE
    -DPNG_SET_OPTION_SUPPORTED
    -DSK_GL
    -DSK_SHAPER_HARFBUZZ_AVAILABLE
    -DSK_UNICODE_AVAILABLE
    # skia/third_party/icu/BUILD.gn
    -DU_DISABLE_RENAMING
    -DU_USING_ICU_NAMESPACE=0
    -DU_DISABLE_RENAMING
    -DSK_USING_THIRD_PARTY_ICU
    -DU_COMMON_IMPLEMENTATION
    -DU_STATIC_IMPLEMENTATION
    -DU_ENABLE_DYLOAD=0
    -DU_I18N_IMPLEMENTATION
    -D_XOPEN_SOURCE=0)

if(CMAKE_BUILD_TYPE STREQUAL "Release")
    add_definitions(-DNDEBUG)
endif()

if(APPLE)
    target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)
    add_definitions(-DSK_ENABLE_API_AVAILABLE
        -DSK_METAL
        -DSK_SHAPER_CORETEXT_AVAILABLE)
    set_target_properties(skija PROPERTIES OUTPUT_NAME "skija_${SKIA_ARCH}")

elseif(UNIX)
    target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/linux)

elseif(WIN32)
    set(CMAKE_C_COMPILER cl)
    set(CMAKE_CXX_COMPILER cl)

    target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/win32)
    target_link_libraries(skija d3d12 dxgi d3dcompiler)

    # target_compile_options(skija PRIVATE /MT)
    # set_property(TARGET skija PROPERTY MSVC_RUNTIME_LIBRARY "MultiThreaded")
    set(CMAKE_CXX_FLAGS_RELEASE "/MT")
    set(CMAKE_CXX_FLAGS_DEBUG "/MTd")
    target_link_options(skija PRIVATE /INCREMENTAL:NO /NODEFAULTLIB:MSVCRT /machine:X64)
    add_definitions(-DNOMINMAX
        -DWIN32_LEAN_AND_MEAN
        -DSK_DIRECT3D
        -DU_NOEXCEPT=)
endif()

# Skia

list(APPEND CMAKE_MODULE_PATH ${CMAKE_SOURCE_DIR}/cmake)
include(FindSkia)
target_link_libraries(skija skottie sksg svg skparagraph skshaper skresources skia ${FREETYPE_LIBRARIES} ${HARFBUZZ_LIBRARIES})
