cmake_minimum_required(VERSION 3.9)

project(skija C CXX)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_BUILD_TYPE Release)

# Sources
file(GLOB SOURCES ${CMAKE_SOURCE_DIR}/src/main/cc/*.cc)
add_library(skija SHARED ${SOURCES})

# JNI
target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)

# Skia
list(APPEND CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake)
include(FindSkia)
target_link_libraries(skija skia skshaper)

# file(GLOB SKIA_LIBS ${CMAKE_SOURCE_DIR}/third_party/skia/out/Release-x64/*.a)
# target_link_libraries(skija ${SKIA_LIBS})
# target_include_directories(skija PRIVATE ${CMAKE_SOURCE_DIR}/third_party/skia)