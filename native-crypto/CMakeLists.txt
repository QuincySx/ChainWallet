cmake_minimum_required(VERSION 3.10)
project(core)

# Combine add_definitions into a single line
add_definitions(-DECMULT_WINDOW_SIZE=15 -DECMULT_GEN_PREC_BITS=4)

set(COMMON_COMPILE_FLAGS USE_FIELD_INV_BUILTIN USE_NUM_NONE USE_SCALAR_INV_BUILTIN)

SET(CMAKE_BUILD_TYPE "Debug")
SET(CMAKE_CXX_FLAGS_DEBUG "$ENV{CXXFLAGS} -O0 -Wall -g2 -ggdb")
SET(CMAKE_CXX_FLAGS_RELEASE "$ENV{CXXFLAGS} -O3 -Wall")

IF (APPLE)
    # Combine SET commands for CMAKE_C_ARCHIVE_FINISH and CMAKE_CXX_ARCHIVE_FINISH
    SET(CMAKE_C_ARCHIVE_FINISH CMAKE_CXX_ARCHIVE_FINISH "<CMAKE_RANLIB> -no_warning_for_no_symbols -c <TARGET>")

    # Combine SET commands for CMAKE_C_ARCHIVE_CREATE and CMAKE_CXX_ARCHIVE_CREATE
    SET(CMAKE_C_ARCHIVE_CREATE CMAKE_CXX_ARCHIVE_CREATE "<CMAKE_AR> qc -S <TARGET> <LINK_FLAGS> <OBJECTS>")

    add_compile_definitions(FORCE_POSIX)
ENDIF ()

if (ANDROID)
    set(COMPILE_FLAGS USE_FIELD_10X26 USE_SCALAR_8X32 HAVE_BUILTIN_EXPECT)
else ()
    set(COMPILE_FLAGS USE_FIELD_5X52 USE_SCALAR_4X64 HAVE_BUILTIN_EXPECT HAVE___INT128)
endif ()

# Remove unnecessary comments and combine compile options into a single line
set(COMPILE_OPTIONS -fPIC -O3 -g -std=gnu99 -W -Wall -Wextra -Wimplicit-function-declaration -Wredundant-decls -Wstrict-prototypes -Wundef -Wshadow -Wpointer-arith -Wformat -Wreturn-type -Wsign-compare -Wmultichar -Wformat-nonliteral -Winit-self -Wuninitialized -Wformat-security -Wno-missing-braces -Wnested-externs -fvisibility=hidden -Wno-overlength-strings -Wno-long-long -Wno-unused-function)

#file(GLOB_RECURSE library_headers
#        *.h
#        ${CMAKE_CURRENT_SOURCE_DIR}/include/*.h)
#file(GLOB_RECURSE library_sources
#        *.c
#        *.cpp)
#
#file(GLOB_RECURSE chacha20_library_headers
#        ${CMAKE_CURRENT_SOURCE_DIR}/chacha20poly1305/*.h)
#file(GLOB_RECURSE chacha20_library_sources
#        ${CMAKE_CURRENT_SOURCE_DIR}/chacha20poly1305/*.c)
#
#file(GLOB_RECURSE aes_library_headers
#        ${CMAKE_CURRENT_SOURCE_DIR}/aes/*.h)
#file(GLOB_RECURSE aes_library_sources
#        ${CMAKE_CURRENT_SOURCE_DIR}/aes/*.c)
#
#file(GLOB_RECURSE ed25519_library_headers
#        ${CMAKE_CURRENT_SOURCE_DIR}/ed25519-donna/*.h)
#file(GLOB_RECURSE ed25519_library_sources
#        ${CMAKE_CURRENT_SOURCE_DIR}/ed25519-donna/*.c)


file(GLOB_RECURSE library_sources
        *.h
        ${CMAKE_CURRENT_SOURCE_DIR}/include/*.h
        *.c
        *.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/chacha20poly1305/*.c
        ${CMAKE_CURRENT_SOURCE_DIR}/chacha20poly1305/*.h
        ${CMAKE_CURRENT_SOURCE_DIR}/aes/*.c
        ${CMAKE_CURRENT_SOURCE_DIR}/aes/*.h
        ${CMAKE_CURRENT_SOURCE_DIR}/ed25519-donna/*.c
        ${CMAKE_CURRENT_SOURCE_DIR}/ed25519-donna/*.h
        )

# Use target_sources instead of listing source files in add_library
add_library(native-crypto STATIC ${library_sources})

target_include_directories(native-crypto PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
target_compile_definitions(native-crypto PRIVATE ${COMMON_COMPILE_FLAGS} ${COMPILE_FLAGS})
target_compile_options(native-crypto PRIVATE ${COMPILE_OPTIONS})

#if (APPLE)
#    set_target_properties(native-crypto PROPERTIES
#            MACOSX_RPATH TRUE
#            ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/libs/osx
#            LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/libs/osx
#            )
#elseif (ANDROID)
#    set_target_properties(native-crypto PROPERTIES
#            ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/libs/android
#            LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/libs/android
#            )
#endif ()

set_target_properties(native-crypto PROPERTIES
        CXX_STANDARD 17
        CXX_EXTENSIONS OFF
        POSITION_INDEPENDENT_CODE ON
        )
