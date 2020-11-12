cmake_minimum_required(VERSION 3.9)

project(metal C CXX)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_BUILD_TYPE Release)

file(GLOB_RECURSE SOURCES ${CMAKE_SOURCE_DIR}/src *.cpp *.mm)
add_library(metal SHARED ${SOURCES})

add_definitions(-DSK_GL -DSK_METAL -DSK_UNICODE_AVAILABLE -DSK_SHAPER_HARFBUZZ_AVAILABLE -DSK_SHAPER_CORETEXT_AVAILABLE)
# target_include_directories(metal PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)
target_link_libraries(metal
  "-framework QuartzCore"
  "-framework Cocoa"
  "-framework Foundation")

list(APPEND CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake)