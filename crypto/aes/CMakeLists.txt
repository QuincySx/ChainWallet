cmake_minimum_required(VERSION 3.10)

set(CMAKE_PROJECT_NAME "aes-wrapper")
project(${CMAKE_PROJECT_NAME})

# Set common optimization flags for Debug mode
set(OPTIMIZATION_FLAGS "-Os -Oz")
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} ${OPTIMIZATION_FLAGS}")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} ${OPTIMIZATION_FLAGS}")

if (APPLE)
    set(CMAKE_VERBOSE_MAKEFILE 1)

    set(JAVA_AWT_LIBRARY NotNeeded)
    set(JAVA_JVM_LIBRARY NotNeeded)
    set(JAVA_AWT_INCLUDE_PATH NotNeeded)

    find_package(Java REQUIRED COMPONENTS Runtime Development)
    find_package(JNI REQUIRED)

    link_directories("${PROJECT_SOURCE_DIR}/src/main/cpp")
    include_directories("${JAVA_INCLUDE_PATH}" "${JAVA_INCLUDE_PATH2}")

    # Support Apple M1
    set(CMAKE_OSX_ARCHITECTURES "x86_64;arm64" CACHE STRING "Build architectures for Mac OS X" FORCE)
elseif (NOT ANDROID)
    set(ANDROID_ABI "mac")
    if (NOT ${JAVA_HOME})
        message(FATAL_ERROR
                "aes-wrapper requires JAVA_HOME to be defined for non-Android builds.")
    endif ()
endif ()

add_subdirectory(../../native-crypto native-crypto)

add_library(
        ${CMAKE_PROJECT_NAME}
        SHARED
        src/main/cpp/aes-wrapper.c
)

if (ANDROID)
    find_library(
            log-lib
            log
    )

    target_link_libraries(
            ${CMAKE_PROJECT_NAME}
            ${log-lib}
            native-crypto
    )
elseif (APPLE)
    target_link_libraries(
            ${CMAKE_PROJECT_NAME}
            native-crypto
    )
endif ()