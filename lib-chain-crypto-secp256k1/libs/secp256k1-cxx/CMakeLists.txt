include_directories(libsecp256k1/include)

add_subdirectory(libsecp256k1)
add_subdirectory(crypto)

add_library(secp256k1-cxx SHARED secp256k1-cxx.cpp)

target_link_libraries(secp256k1-cxx PRIVATE libsecp256k1)
target_link_libraries(secp256k1-cxx PRIVATE crypto)
