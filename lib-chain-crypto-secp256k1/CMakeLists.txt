cmake_minimum_required(VERSION 3.10)

include_directories(libs/secp256k1-cxx)

add_subdirectory(libs/secp256k1-cxx)

add_library(
        secp256k1-wrapper
        SHARED
        libs/secp256k1-wrapper.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        secp256k1-wrapper
        ${log-lib}
        secp256k1-cxx
)