cmake_minimum_required(VERSION 3.10)

include_directories(libs/crypto)

add_subdirectory(libs/crypto)

add_library(
        crypto-wrapper
        SHARED
        libs/crypto-wrapper.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        crypto-wrapper
        ${log-lib}
        crypto
)