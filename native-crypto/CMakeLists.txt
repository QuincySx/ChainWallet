cmake_minimum_required(VERSION 3.4.1)

add_definitions(-DECMULT_WINDOW_SIZE=15)
add_definitions(-DECMULT_GEN_PREC_BITS=4)
set(COMMON_COMPILE_FLAGS USE_FIELD_INV_BUILTIN USE_NUM_NONE USE_SCALAR_INV_BUILTIN)

IF (APPLE)
    # tell ranlib to ignore empty compilation units
    SET(CMAKE_C_ARCHIVE_FINISH "<CMAKE_RANLIB> -no_warning_for_no_symbols -c <TARGET>")
    SET(CMAKE_CXX_ARCHIVE_FINISH "<CMAKE_RANLIB> -no_warning_for_no_symbols -c <TARGET>")
    # prevents ar from invoking ranlib, let CMake do it
    SET(CMAKE_C_ARCHIVE_CREATE "<CMAKE_AR> qc -S <TARGET> <LINK_FLAGS> <OBJECTS>")
    SET(CMAKE_CXX_ARCHIVE_CREATE "<CMAKE_AR> qc -S <TARGET> <LINK_FLAGS> <OBJECTS>")

    add_compile_definitions(FORCE_POSIX)
ENDIF ()

if (ANDROID)
    set(COMPILE_FLAGS USE_FIELD_10X26 USE_SCALAR_8X32 HAVE_BUILTIN_EXPECT)
else ()
    set(COMPILE_FLAGS USE_FIELD_5X52 USE_SCALAR_4X64 HAVE_BUILTIN_EXPECT HAVE___INT128)
endif ()

#set(COMPILE_OPTIONS  -O3 -g -std=gnu99 -W -Wall -Wextra -Wimplicit-function-declaration -Wredundant-decls -Wstrict-prototypes -Wundef -Wshadow -Wpointer-arith -Wformat -Wreturn-type -Wsign-compare -Wmultichar -Wformat-nonliteral -Winit-self -Wuninitialized -Wformat-security -Wno-missing-braces -Werror)
#set(COMPILE_OPTIONS -fPIC -O3 -g -std=gnu99 -W -Wall -Wextra -Wimplicit-function-declaration -Wredundant-decls -Wstrict-prototypes -Wundef -Wshadow -Wpointer-arith -Wformat -Wreturn-type -Wsign-compare -Wmultichar -Wformat-nonliteral -Winit-self -Wuninitialized -Wformat-security -Wno-missing-braces -Werror -Wcast-align -Wnested-externs -fvisibility=hidden -Wno-overlength-strings -Wno-long-long -Wno-unused-function)
set(COMPILE_OPTIONS -fPIC -O3 -g -std=gnu99 -W -Wall -Wextra -Wimplicit-function-declaration -Wredundant-decls -Wstrict-prototypes -Wundef -Wshadow -Wpointer-arith -Wformat -Wreturn-type -Wsign-compare -Wmultichar -Wformat-nonliteral -Winit-self -Wuninitialized -Wformat-security -Wno-missing-braces -Wnested-externs -fvisibility=hidden -Wno-overlength-strings -Wno-long-long -Wno-unused-function)


project(core)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library(native-crypto

        # Sets the library as a shared library.
        STATIC

        # Provides a relative path to your source file(s).
        options.h
        include/jni_method.h
        jni_method.c
        include/base58.h
        base58.c
        include/bignum.h
        bignum.c
        include/der.h
        der.c
        include/ecdsa.h
        ecdsa.c
        include/hexstring.h
        hexstring.c
        include/hmac.h
        include/hmac_drbg.h
        hmac.c
        hmac_drbg.c
        include/memzero.h
        memzero.c
        include/pbkdf2.h
        pbkdf2.c
        include/rand.h
        rand.c
        include/rfc6979.h
        rfc6979.c
        include/ripemd160.h
        ripemd160.c
        include/secp256k1.h
        secp256k1.c
        include/sha2.h
        sha2.c
        include/sha3.h
        sha3.c
        aes/aes.h
        aes/aes_modes.c
        aes/aescrypt.c
        aes/aeskey.c
        aes/aesopt.h
        aes/aestab.c
        aes/aestab.h
        aes/aestst.c
        aes/aestst.h
        chacha20poly1305/chacha20poly1305.c
        chacha20poly1305/chacha20poly1305.h
        chacha20poly1305/chacha_merged.c
        chacha20poly1305/ecrypt-config.h
        chacha20poly1305/ecrypt-machine.h
        chacha20poly1305/ecrypt-portable.h
        chacha20poly1305/ecrypt-sync.h
        chacha20poly1305/ecrypt-types.h
        chacha20poly1305/poly1305-donna-32.h
        chacha20poly1305/poly1305-donna.c
        chacha20poly1305/poly1305-donna.h
        chacha20poly1305/rfc7539.c
        chacha20poly1305/rfc7539.h
        ed25519-donna/curve25519-donna-32bit.c
        ed25519-donna/curve25519-donna-32bit.h
        ed25519-donna/curve25519-donna-helpers.c
        ed25519-donna/curve25519-donna-helpers.h
        ed25519-donna/curve25519-donna-scalarmult-base.c
        ed25519-donna/curve25519-donna-scalarmult-base.h
        ed25519-donna/ed25519.h
        ed25519-donna/ed25519.c
        ed25519-donna/ed25519-donna.h
        ed25519-donna/ed25519-donna-32bit-tables.c
        ed25519-donna/ed25519-donna-32bit-tables.h
        ed25519-donna/ed25519-donna-basepoint-table.c
        ed25519-donna/ed25519-donna-basepoint-table.h
        ed25519-donna/ed25519-donna-impl-base.c
        ed25519-donna/ed25519-donna-impl-base.h
        ed25519-donna/ed25519-donna-portable.h
        ed25519-donna/ed25519-hash-custom-keccak.h
        ed25519-donna/ed25519-hash-custom-sha3.h
        ed25519-donna/ed25519-hash-custom.h
        ed25519-donna/ed25519-keccak.c
        ed25519-donna/ed25519-keccak.h
        ed25519-donna/ed25519-sha3.c
        ed25519-donna/ed25519-sha3.h
        ed25519-donna/modm-donna-32bit.c
        ed25519-donna/modm-donna-32bit.h
        )

target_include_directories(native-crypto PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
target_compile_definitions(native-crypto PUBLIC ${COMMON_COMPILE_FLAGS} ${COMPILE_FLAGS})
target_compile_options(native-crypto PUBLIC ${COMPILE_OPTIONS})
set_target_properties(native-crypto PROPERTIES
        CXX_STANDARD 17
        CXX_EXTENSIONS OFF
        POSITION_INDEPENDENT_CODE ON
        )

