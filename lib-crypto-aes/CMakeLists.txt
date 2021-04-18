cmake_minimum_required(VERSION 3.4.1)

# Debug模式设置为最低优化等级，这样能尽可能多的保留Debug信息
#set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -O0")
#set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -O0")

# 正常模式
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Os -Oz")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Os -Oz")

set(CMAKE_PROJECT_NAME "aes-wrapper")

project(${CMAKE_PROJECT_NAME})

if (APPLE)
    set(CMAKE_VERBOSE_MAKEFILE 1)

    set(JAVA_AWT_LIBRARY NotNeeded)
    set(JAVA_JVM_LIBRARY NotNeeded)
    set(JAVA_AWT_INCLUDE_PATH NotNeeded)

    find_package(Java REQUIRED COMPONENTS Runtime Development)
    find_package(JNI REQUIRED)

    link_directories("${PROJECT_SOURCE_DIR}/src/main/cpp")
    include_directories("${JAVA_INCLUDE_PATH}" "${JAVA_INCLUDE_PATH2}")
endif ()

if (NOT ANDROID)
    set(ANDROID_ABI "mac")
    if (NOT ${JAVA_HOME})
        message(FATAL_ERROR
                "aes-wrapper requires JAVA_HOME to be defined for non-Android builds.")
    endif ()
endif ()

# 对于x86和x86-64显示的设置-Os参数
# 注意一旦显示设置了编译参数则意味着默认的编译参数将全部抛弃，所以需要一定要设置-s这个对减小体积起主要作用的参数
if (${ANDROID_ABI} STREQUAL "x86")
    set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Os -Oz")
    set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Os -Oz")
endif ()

add_subdirectory(../native-crypto native-crypto)

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
endif ()

if (APPLE)
    target_link_libraries(
            ${CMAKE_PROJECT_NAME}
            native-crypto
    )
    # Support Apple M1
    SET(CMAKE_OSX_ARCHITECTURES "x86_64;arm64" CACHE STRING "Build architectures for Mac OS X" FORCE)
endif ()