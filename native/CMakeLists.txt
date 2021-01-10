cmake_minimum_required(VERSION 3.9)
project(skija C CXX)
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Sources

file(GLOB SOURCES ${CMAKE_SOURCE_DIR}/src/*.cc)
file(GLOB IMPL_SOURCES ${CMAKE_SOURCE_DIR}/src/impl/*.cc)
file(GLOB PARAGRAPH_SOURCES ${CMAKE_SOURCE_DIR}/src/paragraph/*.cc)
file(GLOB SHAPER_SOURCES ${CMAKE_SOURCE_DIR}/src/shaper/*.cc)
file(GLOB SVG_SOURCES ${CMAKE_SOURCE_DIR}/src/svg/*.cc)
file(GLOB SKOTTIE_SOURCES ${CMAKE_SOURCE_DIR}/src/skottie/*.cc)
file(GLOB SKSG_SOURCES ${CMAKE_SOURCE_DIR}/src/sksg/*.cc)
add_library(skija SHARED ${SOURCES} ${IMPL_SOURCES} ${PARAGRAPH_SOURCES} ${SHAPER_SOURCES} ${SVG_SOURCES} ${SKOTTIE_SOURCES} ${SKSG_SOURCES})

add_definitions(-DFT2_BUILD_LIBRARY
    -DFT_CONFIG_MODULES_H=<include/freetype-android/ftmodule.h>
    -DFT_CONFIG_OPTIONS_H=<include/freetype-android/ftoption.h>
    -DPNG_INTEL_SSE
    -DPNG_SET_OPTION_SUPPORTED
    -DSK_GL
    -DSK_SHAPER_HARFBUZZ_AVAILABLE
    -DSK_UNICODE_AVAILABLE)

if(CMAKE_BUILD_TYPE STREQUAL "Release")
    add_definitions(-DNDEBUG)
endif()

if(APPLE)
    target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)
    add_definitions(-DSK_ENABLE_API_AVAILABLE
        -DSK_METAL
        -DSK_SHAPER_CORETEXT_AVAILABLE)

elseif(UNIX)
    target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/linux)

elseif(WIN32)
    set(CMAKE_C_COMPILER cl)
    set(CMAKE_CXX_COMPILER cl)

    target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/win32)

    # target_compile_options(skija PRIVATE /MT)
    # set_property(TARGET skija PROPERTY MSVC_RUNTIME_LIBRARY "MultiThreaded")
    set(CMAKE_CXX_FLAGS_RELEASE "/MT")
    set(CMAKE_CXX_FLAGS_DEBUG "/MTd")
    target_link_options(skija PRIVATE /INCREMENTAL:NO /NODEFAULTLIB:MSVCRT /machine:X64)
    add_definitions(-DNOMINMAX -DWIN32_LEAN_AND_MEAN)
endif()

# Skia

list(APPEND CMAKE_MODULE_PATH ${CMAKE_SOURCE_DIR}/cmake)
include(FindSkia)
target_link_libraries(skija svg skparagraph skshaper skia ${FREETYPE_LIBRARIES} ${HARFBUZZ_LIBRARIES} skottie sksg)
