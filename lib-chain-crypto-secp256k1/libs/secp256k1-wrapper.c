//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>
#include <include/secp256k1.h>
#include <string.h>

#define PUBLIC_KEY_SIZE 65

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_createPublicKey(JNIEnv *env,
                                                             jobject byteObj /* this */,
                                                             jbyteArray privKeyBytes_jbyteArray,
                                                             jboolean compressed_jbool) {
    unsigned char *privateKey = (unsigned char *) (*env)->GetByteArrayElements(env,
                                                                               privKeyBytes_jbyteArray,
                                                                               0);
    int compressed = compressed_jbool == JNI_TRUE;

    secp256k1_context *ctx = secp256k1_context_create(SECP256K1_CONTEXT_SIGN);

    secp256k1_pubkey pubkey;
    int ret = secp256k1_ec_pubkey_create(ctx, &pubkey, privateKey);
    if (ret != 1) {
        return 0;
    }

    unsigned char pbKey[PUBLIC_KEY_SIZE] = {0};
    size_t publicKeySize = PUBLIC_KEY_SIZE;

    secp256k1_ec_pubkey_serialize(
            ctx, pbKey, &publicKeySize, &pubkey,
            compressed ? SECP256K1_EC_COMPRESSED : SECP256K1_EC_UNCOMPRESSED);

    int size = PUBLIC_KEY_SIZE;

    jbyteArray nullBytes = (*env)->NewByteArray(env, size);
    (*env)->SetByteArrayRegion(env, nullBytes, 0, size, (jbyte *) pbKey);

    return nullBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_sign(JNIEnv *env,
                                                  jobject byteObj /* this */,
                                                  jbyteArray private_key_jbytearray,
                                                  jbyteArray message_jbytearray) {
    const unsigned char *privateKey = (const unsigned char *) (*env)->GetByteArrayElements(
            env, private_key_jbytearray, 0);

    const unsigned char *messages = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                         message_jbytearray,
                                                                                         0);

    secp256k1_context *ctx = secp256k1_context_create(SECP256K1_CONTEXT_SIGN);
    // Make signature.
    secp256k1_ecdsa_signature sig;
    int ret = secp256k1_ecdsa_sign(ctx, &sig, messages, privateKey,
                                   secp256k1_nonce_function_rfc6979, NULL);
    if (ret != 1) {
        // Failed to sign.
        return 0;
    }

    // Serialize signature.
    uint8_t *sigOut[72] = {0};
    size_t sigOutSize = 72;
    ret = secp256k1_ecdsa_signature_serialize_der(
            ctx, sigOut[0], &sigOutSize, &sig);
    if (ret != 1) {
        // Failed to serialize.
        return 0;
    }

    jbyteArray outputBytes = (*env)->NewByteArray(env, sigOutSize);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, sigOutSize, (jbyte *) sigOut);

    return outputBytes;
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
//        static_assert(sizeof(size_t) >= 4, "size_t too small");
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
//        static_assert(sizeof(size_t) >= 4, "size_t too small");
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

JNIEXPORT jboolean JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_verify(JNIEnv *env,
                                                    jobject byteObj /* this */,
                                                    jbyteArray public_key_jbytearray,
                                                    jbyteArray signature_jbytearray,
                                                    jbyteArray message_jbytearray) {
    const unsigned char *publicKey = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                          public_key_jbytearray,
                                                                                          0);

    const unsigned char *signature = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                          signature_jbytearray,
                                                                                          0);

    const unsigned char *message = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                        message_jbytearray,
                                                                                        0);

    const int pubKeySize = (*env)->GetArrayLength(env, public_key_jbytearray);
    const int signatureSize = (*env)->GetArrayLength(env, signature_jbytearray);

    secp256k1_context *ctx = secp256k1_context_create(SECP256K1_CONTEXT_VERIFY);
    if (pubKeySize != PUBLIC_KEY_SIZE) {
        return JNI_FALSE;
    }
    if (signatureSize != 72) {
        return JNI_FALSE;
    }

    // Parse public key.
    secp256k1_pubkey pubkey;
    if (!secp256k1_ec_pubkey_parse(ctx, &pubkey, publicKey, pubKeySize)) {
        return JNI_FALSE;
    }

    // Parse signature.
    secp256k1_ecdsa_signature sig;
    if (!ecdsa_signature_parse_der_lax(ctx, &sig, signature, signatureSize)) {
        return JNI_FALSE;
    }

    secp256k1_ecdsa_signature_normalize(ctx, &sig, &sig);
    jboolean ret = secp256k1_ecdsa_verify(ctx, &sig, message, &pubkey);
    secp256k1_context_destroy(ctx);
    return ret;
}