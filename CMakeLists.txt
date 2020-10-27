cmake_minimum_required(VERSION 3.9)

project(skija C CXX)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_BUILD_TYPE Release)
if(WIN32)
set(CMAKE_C_COMPILER cl)
set(CMAKE_CXX_COMPILER cl)
endif()

# Sources
file(GLOB SOURCES ${CMAKE_SOURCE_DIR}/src/main/cc/*.cc)
file(GLOB IMPL_SOURCES ${CMAKE_SOURCE_DIR}/src/main/cc/impl/*.cc)
file(GLOB SHAPER_SOURCES ${CMAKE_SOURCE_DIR}/src/main/cc/shaper/*.cc)
file(GLOB PARAGRAPH_SOURCES ${CMAKE_SOURCE_DIR}/src/main/cc/paragraph/*.cc)
add_definitions(-DSK_GL -DSK_SHAPER_HARFBUZZ_AVAILABLE)
add_library(skija SHARED ${SOURCES} ${IMPL_SOURCES} ${SHAPER_SOURCES} ${PARAGRAPH_SOURCES})

# JNI
if(APPLE)
add_definitions(-DSK_SHAPER_CORETEXT_AVAILABLE -DSK_METAL)
target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)
elseif(UNIX)
target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/linux)
elseif(WIN32)
target_include_directories(skija PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/win32)
endif()

# Skia
list(APPEND CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake)
include(FindSkia)
target_link_libraries(skija skia skshaper skparagraph)

if(WIN32)
# target_compile_options(skija PRIVATE /MT)
# set_property(TARGET skija PROPERTY MSVC_RUNTIME_LIBRARY "MultiThreaded")
set(CMAKE_CXX_FLAGS_RELEASE "/MT")
set(CMAKE_CXX_FLAGS_DEBUG "/MTd")
target_link_options(skija PRIVATE /INCREMENTAL:NO /NODEFAULTLIB:MSVCRT)
endif()
