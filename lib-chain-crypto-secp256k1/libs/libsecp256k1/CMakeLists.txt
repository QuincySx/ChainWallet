# Source code see https://github.com/bitcoin-core/secp256k1
include_directories(include)

set(CMAKE_INCLUDE_CURRENT_DIR ON)

#add_definitions(-DHAVE_CONFIG_H=1)

add_definitions(-DECMULT_WINDOW_SIZE=15)
add_definitions(-DECMULT_GEN_PREC_BITS=4)
set(COMMON_COMPILE_FLAGS USE_FIELD_INV_BUILTIN USE_NUM_NONE USE_SCALAR_INV_BUILTIN)

if (ANDROID)
    set(COMPILE_FLAGS USE_FIELD_10X26 USE_SCALAR_8X32 HAVE_BUILTIN_EXPECT)
else()
    set(COMPILE_FLAGS USE_FIELD_5X52 USE_SCALAR_4X64 HAVE_BUILTIN_EXPECT HAVE___INT128)
endif()

set(COMPILE_OPTIONS -fPIC -O3 -W -std=c11 -pedantic -Wall -Wextra -Wcast-align -Wnested-externs -Wshadow -Wstrict-prototypes -Wno-unused-function -Wno-long-long -Wno-overlength-strings -fvisibility=hidden)
#set(COMPILE_OPTIONS -fPIC -O3 -W -std=c89 -pedantic -Wall -Wextra -Wcast-align -Wnested-externs -Wshadow -Wstrict-prototypes -Wno-unused-function -Wno-long-long -Wno-overlength-strings -fvisibility=hidden)

aux_source_directory(src DIR_SRCS)

#add_library(libsecp256k1 src/secp256k1.c include/secp256k1.h)
#add_library(libsecp256k1 src/secp256k1.c include/secp256k1.h ${DIR_SRCS})
add_library(libsecp256k1 include/secp256k1.h ${DIR_SRCS})

target_compile_definitions(libsecp256k1 PRIVATE ${COMMON_COMPILE_FLAGS} ${COMPILE_FLAGS})
target_include_directories(libsecp256k1 PRIVATE ${CMAKE_SOURCE_DIR} ${CMAKE_SOURCE_DIR}/src)
target_compile_options(libsecp256k1 PRIVATE ${COMPILE_OPTIONS})