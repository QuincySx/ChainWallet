# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# 正常模式
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Os -Oz")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Os -Oz")

# 对于x86和x86-64显示的设置-Os参数
# 注意一旦显示设置了编译参数则意味着默认的编译参数将全部抛弃，所以需要一定要设置-s这个对减小体积起主要作用的参数
if (${ANDROID_ABI} STREQUAL "x86")
    set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Os -Oz")
    set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Os -Oz")
endif ()

include_directories(src/main/cpp)

add_library( # Sets the name of the library.
        # 这个名字防止太极注入破解,太极会注入 libdexinterpret.so
        dexinterpret

        SHARED

        src/main/cpp/authority-lib.c
        src/main/cpp/valid.c)

find_library(log-lib log)

target_link_libraries(dexinterpret ${log-lib})