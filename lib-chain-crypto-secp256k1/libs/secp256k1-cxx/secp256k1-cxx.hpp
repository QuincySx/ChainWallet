#ifndef SECP256K1_CPP_H
#define SECP256K1_CPP_H

#include "libsecp256k1/include/secp256k1.h"

#include <stdexcept>
#include <stdint.h>
#include <vector>

static constexpr size_t PUBLIC_KEY_SIZE = 65;


class Secp256K1Exception : public std::runtime_error {
public:
    Secp256K1Exception(const char *error) noexcept
            : std::runtime_error(error) {
    }

    const char *what() const noexcept {
        return std::runtime_error::what();
    }
};

std::vector<uint8_t>
createPublicKeyFromPriv(const std::vector<uint8_t> &privateKey, bool compressed = true);

std::tuple<std::vector<uint8_t>, bool>
Sign(const std::vector<uint8_t> &privateKey, const uint8_t *hash);

bool
Verify(const uint8_t *msgHash, const std::vector<uint8_t> &sig, const std::vector<uint8_t> &pubKey);

#endif
