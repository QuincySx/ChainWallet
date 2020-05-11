#include "secp256k1-cxx.hpp"

#include <cassert>
#include <chrono>
#include <cstring>
#include <iostream>
#include <random>
#include <tuple>
#include <vector>

std::vector<uint8_t>
createPublicKeyFromPriv(const std::vector<uint8_t> &privateKey, bool compressed) {
    auto ctx = secp256k1_context_create(SECP256K1_CONTEXT_SIGN);
    // Calculate public key.
    std::vector<uint8_t> pubKey;
    secp256k1_pubkey pubkey;
    int ret = secp256k1_ec_pubkey_create(ctx, &pubkey, privateKey.data());
    if (ret != 1) {
        return std::vector<uint8_t>();
    }

    // Serialize public key.
    size_t outSize = PUBLIC_KEY_SIZE;
    pubKey.resize(outSize);
    secp256k1_ec_pubkey_serialize(
            ctx, pubKey.data(), &outSize, &pubkey,
            compressed ? SECP256K1_EC_COMPRESSED : SECP256K1_EC_UNCOMPRESSED);
    pubKey.resize(outSize);

    // Succeed.
    return pubKey;
}

std::tuple<std::vector<uint8_t>, bool>
Sign(const std::vector<uint8_t> &privKey, const uint8_t *hash) {
    auto ctx = secp256k1_context_create(SECP256K1_CONTEXT_SIGN);
    // Make signature.
    secp256k1_ecdsa_signature sig;
    int ret = secp256k1_ecdsa_sign(ctx, &sig, hash, privKey.data(),
                                   secp256k1_nonce_function_rfc6979, nullptr);
    if (ret != 1) {
        // Failed to sign.
        return std::make_tuple(std::vector<uint8_t>(), false);
    }

    // Serialize signature.
    std::vector<uint8_t> sigOut(72);
    size_t sigOutSize = 72;
    ret = secp256k1_ecdsa_signature_serialize_der(
            ctx, &sigOut[0], &sigOutSize, &sig);
    if (ret != 1) {
        // Failed to serialize.
        return std::make_tuple(std::vector<uint8_t>(), false);
    }

    // Returns
    sigOut.resize(sigOutSize);
    return std::make_tuple(sigOut, true);
}

/** This function is taken from the libsecp256k1 distribution and implements
 *  DER parsing for ECDSA signatures, while supporting an arbitrary subset of
 *  format violations.
 *
 *  Supported violations include negative integers, excessive padding, garbage
 *  at the end, and overly long length descriptors. This is safe to use in
 *  Bitcoin because since the activation of BIP66, signatures are verified to be
 *  strict DER before being passed to this module, and we know it supports all
 *  violations present in the blockchain before that point.
 */
static int
ecdsa_signature_parse_der_lax(const secp256k1_context *ctx, secp256k1_ecdsa_signature *sig,
                              const unsigned char *input, size_t inputlen) {
    size_t rpos, rlen, spos, slen;
    size_t pos = 0;
    size_t lenbyte;
    unsigned char tmpsig[64] = {0};
    int overflow = 0;

    /* Hack to initialize sig with a correctly-parsed but invalid signature. */
    secp256k1_ecdsa_signature_parse_compact(ctx, sig, tmpsig);

    /* Sequence tag byte */
    if (pos == inputlen || input[pos] != 0x30) {
        return 0;
    }
    pos++;

    /* Sequence length bytes */
    if (pos == inputlen) {
        return 0;
    }
    lenbyte = input[pos++];
    if (lenbyte & 0x80) {
        lenbyte -= 0x80;
        if (lenbyte > inputlen - pos) {
            return 0;
        }
        pos += lenbyte;
    }

    /* Integer tag byte for R */
    if (pos == inputlen || input[pos] != 0x02) {
        return 0;
    }
    pos++;

    /* Integer length for R */
    if (pos == inputlen) {
        return 0;
    }
    lenbyte = input[pos++];
    if (lenbyte & 0x80) {
        lenbyte -= 0x80;
        if (lenbyte > inputlen - pos) {
            return 0;
        }
        while (lenbyte > 0 && input[pos] == 0) {
            pos++;
            lenbyte--;
        }
        static_assert(sizeof(size_t) >= 4, "size_t too small");
        if (lenbyte >= 4) {
            return 0;
        }
        rlen = 0;
        while (lenbyte > 0) {
            rlen = (rlen << 8) + input[pos];
            pos++;
            lenbyte--;
        }
    } else {
        rlen = lenbyte;
    }
    if (rlen > inputlen - pos) {
        return 0;
    }
    rpos = pos;
    pos += rlen;

    /* Integer tag byte for S */
    if (pos == inputlen || input[pos] != 0x02) {
        return 0;
    }
    pos++;

    /* Integer length for S */
    if (pos == inputlen) {
        return 0;
    }
    lenbyte = input[pos++];
    if (lenbyte & 0x80) {
        lenbyte -= 0x80;
        if (lenbyte > inputlen - pos) {
            return 0;
        }
        while (lenbyte > 0 && input[pos] == 0) {
            pos++;
            lenbyte--;
        }
        static_assert(sizeof(size_t) >= 4, "size_t too small");
        if (lenbyte >= 4) {
            return 0;
        }
        slen = 0;
        while (lenbyte > 0) {
            slen = (slen << 8) + input[pos];
            pos++;
            lenbyte--;
        }
    } else {
        slen = lenbyte;
    }
    if (slen > inputlen - pos) {
        return 0;
    }
    spos = pos;

    /* Ignore leading zeroes in R */
    while (rlen > 0 && input[rpos] == 0) {
        rlen--;
        rpos++;
    }
    /* Copy R value */
    if (rlen > 32) {
        overflow = 1;
    } else {
        memcpy(tmpsig + 32 - rlen, input + rpos, rlen);
    }

    /* Ignore leading zeroes in S */
    while (slen > 0 && input[spos] == 0) {
        slen--;
        spos++;
    }
    /* Copy S value */
    if (slen > 32) {
        overflow = 1;
    } else {
        memcpy(tmpsig + 64 - slen, input + spos, slen);
    }

    if (!overflow) {
        overflow = !secp256k1_ecdsa_signature_parse_compact(ctx, sig, tmpsig);
    }
    if (overflow) {
        /* Overwrite the result again with a correctly-parsed but invalid
           signature if parsing failed. */
        memset(tmpsig, 0, 64);
        secp256k1_ecdsa_signature_parse_compact(ctx, sig, tmpsig);
    }
    return 1;
}

bool
Verify(const uint8_t *msgHash, const std::vector<uint8_t> &sign,
       const std::vector<uint8_t> &pubKey) {
    auto ctx = secp256k1_context_create(SECP256K1_CONTEXT_VERIFY);
    if (pubKey.size() != PUBLIC_KEY_SIZE) {
        throw Secp256K1Exception("Invalid public key size");
    }
    if (sign.size() != 72) {
        throw Secp256K1Exception("Invalid signature size");
    }

    // Parse public key.
    secp256k1_pubkey pubkey;
    if (!secp256k1_ec_pubkey_parse(ctx, &pubkey, pubKey.data(),
                                   pubKey.size())) {
        return false;
    }

    // Parse signature.
    secp256k1_ecdsa_signature sig;
    if (!ecdsa_signature_parse_der_lax(ctx, &sig, sign.data(), sign.size())) {
        return false;
    }

    secp256k1_ecdsa_signature_normalize(ctx, &sig, &sig);
    bool ret = secp256k1_ecdsa_verify(ctx, &sig, msgHash, &pubkey);
    secp256k1_context_destroy(ctx);
    return ret;
}