cmake_minimum_required(VERSION 3.10)

# Debug模式设置为最低优化等级，这样能尽可能多的保留Debug信息
#set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -O0")
#set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -O0")

set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Oz")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Oz")

# 对于x86和x86-64显示的设置-Os参数
# 注意一旦显示设置了编译参数则意味着默认的编译参数将全部抛弃，所以需要一定要设置-s这个对减小体积起主要作用的参数
if (${ANDROID_ABI} STREQUAL "x86")
    set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s -Os")
    set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s -Os")
endif ()

include_directories(libs/libsecp256k1)

add_subdirectory(libs/libsecp256k1)

add_library(
        secp256k1-wrapper
        SHARED
#        libs/secp256k1-wrapper.cpp
        libs/secp256k1-wrapper.c
)

find_library(
        log-lib
        log
)

target_link_libraries(
        secp256k1-wrapper
        ${log-lib}
        libsecp256k1
)