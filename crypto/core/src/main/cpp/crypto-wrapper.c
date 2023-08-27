//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>

#include <string.h>
#include <stdlib.h>
#include "include/base58.h"
#include "include/sha2.h"
#include "include/hmac.h"
#include "include/ripemd160.h"
#include "include/hexstring.h"
#include "include/der.h"
#include "include/sha3.h"
#include "include/jni_method.h"
#include "include/blake2b.h"
#include "include/blake2s.h"
#include "include/blake256.h"
#include "include/pbkdf2.h"
#include "include/base32.h"

JNIEXPORT jstring JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_hexToStr(JNIEnv *env, jobject thiz /* this */, jbyteArray j_bytes) {
    int data_size;
    unsigned char *bytes = critical_array_to_c(env, j_bytes, &data_size);

    int out_len = data_size * 2;
    char *out = (char *) calloc(out_len, sizeof(char));
    if (!out) {
        critical_array_release(env, j_bytes, bytes);
        return NULL;
    }

    hexToStr(out, bytes, data_size, 1);
    critical_array_release(env, j_bytes, bytes);

    jstring result = (*env)->NewStringUTF(env, out);
    free(out);  // Assuming base58_encode allocates memory that needs to be freed
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_strToHex(JNIEnv *env, jobject thiz, jstring j_date) {
    int date_len = (*env)->GetStringUTFLength(env, j_date);
    int out_len = date_len / 2;
    char *out = (char *) calloc(out_len, sizeof(char));
    if (!out) {
        return NULL;
    }

    const char *date_str = (*env)->GetStringUTFChars(env, j_date, NULL);
    strToHex(out, date_str, out_len);
    (*env)->ReleaseStringUTFChars(env, j_date, date_str);

    jbyteArray result = (*env)->NewByteArray(env, out_len);
    (*env)->SetByteArrayRegion(env, result, 0, out_len, (jbyte *) out);
    free(out);
    return result;
}

JNIEXPORT jstring JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_base32Encode(JNIEnv *env, jclass cls, jbyteArray inputArray, jboolean jpadding) {
    int inputLen;
    unsigned char *input = critical_array_to_c(env, inputArray, &inputLen);

    const char *alphabet = BASE32_ALPHABET_RFC4648;
    size_t outputLen = base32_encoded_length(inputLen);
    char *output = (char *) calloc(outputLen + 1, sizeof(char));

    char *ret = base32_encode_padding((const uint8_t *) input, inputLen, output, outputLen + 1, alphabet, jpadding);
    critical_array_release(env, inputArray, input);
    if (ret == NULL) {
        return NULL;
    }

    jstring result = (*env)->NewStringUTF(env, output);
    free(output);  // Assuming base58_encode allocates memory that needs to be freed
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_base32Decode(JNIEnv *env, jclass cls, jstring j_date, jboolean jpadding) {
    const char *input = (*env)->GetStringUTFChars(env, j_date, NULL);
    int inputLen = (*env)->GetStringUTFLength(env, j_date);

    const char *alphabet = BASE32_ALPHABET_RFC4648;
    size_t outputLen = base32_decoded_length(inputLen);
    uint8_t *output = (uint8_t *) calloc(outputLen, sizeof(uint8_t));

    uint8_t *ret = base32_decode_padding((const char *) input, inputLen, output, outputLen, alphabet, jpadding);
    (*env)->ReleaseStringUTFChars(env, j_date, input);
    if (ret == NULL) {
        return NULL;
    }

    jbyteArray outputArray = (*env)->NewByteArray(env, outputLen);
    (*env)->SetByteArrayRegion(env, outputArray, 0, outputLen, (jbyte *) output);
    free(output);
    return outputArray;
}

/**
 * @brief creates public key from given bytes(private key) and returns it in uncompressed form
 * @param env
 * @param byteObj
 * @param privKeyBytes
 * @return public key byte[]
 */
JNIEXPORT jstring JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_base58Encode(JNIEnv *env, jobject thiz, jbyteArray j_bytes) {
    int bytes_size;
    unsigned char *bytes = critical_array_to_c(env, j_bytes, &bytes_size);

    int out_len;
    unsigned char *out = base58_encode(bytes, bytes_size, &out_len);

    critical_array_release(env, j_bytes, bytes);

    jstring result = (*env)->NewStringUTF(env, out);
    free(out);  // Assuming base58_encode allocates memory that needs to be freed
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_base58Decode(JNIEnv *env, jobject thiz, jstring j_date) {
    const char *bytes = (*env)->GetStringUTFChars(env, j_date, NULL);
    int bytesSize = (*env)->GetStringUTFLength(env, j_date);

    int out_len;
    unsigned char *out = base58_decode(bytes, bytesSize, &out_len);

    (*env)->ReleaseStringUTFChars(env, j_date, bytes);

    jbyteArray returnBytes = (*env)->NewByteArray(env, out_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, out_len, (jbyte *) out);
    free(out);  // Assuming base58_decode allocates memory that needs to be freed
    return returnBytes;
}

JNIEXPORT jstring JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_base58EncodeCheck(JNIEnv *env, jobject thiz, jbyteArray j_bytes) {
    int bytes_size;
    unsigned char *bytes = critical_array_to_c(env, j_bytes, &bytes_size);

    int out_len;
    unsigned char *out = base58_encode_check(bytes, bytes_size, &out_len);
    critical_array_release(env, j_bytes, bytes);

    jstring result = (*env)->NewStringUTF(env, out);
    free(out);  // Assuming base58_encode allocates memory that needs to be freed
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_base58DecodeCheck(JNIEnv *env, jobject thiz, jstring date_jbyteArray) {
    const char *bytes = (const char *) (*env)->GetStringUTFChars(env, date_jbyteArray, 0);
    int bytesSize = (*env)->GetStringUTFLength(env, date_jbyteArray);

    int out_len;
    unsigned char *out = base58_decode_check(bytes, bytesSize, &out_len);

    (*env)->ReleaseStringUTFChars(env, date_jbyteArray, bytes);

    jbyteArray returnBytes = (*env)->NewByteArray(env, out_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, out_len, (jbyte *) out);
    free(out);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sha256(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    unsigned char *date = copy_array_to_c(env, j_date, &data_size);
    sha256_Raw(date, data_size, digest);

    copy_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sha512(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA512_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    unsigned char *date = copy_array_to_c(env, j_date, &data_size);
    sha512_Raw(date, data_size, digest);

    copy_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA512_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA512_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_doubleSha256(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    unsigned char *date = copy_array_to_c(env, j_date, &data_size);
    sha256_Raw(date, data_size, digest);
    sha256_Raw(digest, SHA256_DIGEST_LENGTH, digest);

    copy_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_ripemd160(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    int data_size;
    unsigned char *message = copy_array_to_c(env, j_date, &data_size);

    uint8_t *output = (uint8_t *) calloc(RIPEMD160_DIGEST_LENGTH, sizeof(uint8_t));
    ripemd160(message, data_size, output);

    copy_array_release(env, j_date, message);

    jbyteArray returnBytes = (*env)->NewByteArray(env, RIPEMD160_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, RIPEMD160_DIGEST_LENGTH, (jbyte *) output);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sha3_1224(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_224_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    sha3_224(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_224_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_224_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sha3_1256(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    sha3_256(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sha3_1384(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_384_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    sha3_384(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_384_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_384_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sha3_1512(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_512_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    sha3_512(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_512_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_512_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_doubleSha3_1256(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    sha3_256(date, data_size, digest);
    sha3_256(digest, SHA3_256_DIGEST_LENGTH, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_keccak_1224(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_224_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    keccak_224(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_224_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_224_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_keccak_1256(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    keccak_256(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_keccak_1384(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_384_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    keccak_384(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_384_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_384_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_keccak_1512(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_512_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    keccak_512(date, data_size, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_512_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_512_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_doubleKeccak_1256(JNIEnv *env, jobject thiz, jbyteArray j_date) {
    uint8_t *digest = (uint8_t *) calloc(SHA3_256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int data_size;
    uint8_t *date = critical_array_to_c(env, j_date, &data_size);

    keccak_256(date, data_size, digest);
    keccak_256(digest, SHA3_256_DIGEST_LENGTH, digest);

    critical_array_release(env, j_date, date);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_hmac_1sha256(JNIEnv *env, jobject thiz, jbyteArray j_key, jbyteArray j_message) {
    uint8_t *digest = (uint8_t *) calloc(SHA256_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int key_size, message_size;
    unsigned char *key = critical_array_to_c(env, j_key, &key_size);
    unsigned char *message = critical_array_to_c(env, j_message, &message_size);

    hmac_sha256(key, key_size, message, message_size, digest);

    critical_array_release(env, j_key, key);
    critical_array_release(env, j_message, message);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_hmac_1sha512(JNIEnv *env, jobject thiz, jbyteArray j_key, jbyteArray j_message) {
    uint8_t *digest = (uint8_t *) calloc(SHA512_DIGEST_LENGTH, sizeof(uint8_t));
    if (!digest) {
        return NULL;
    }

    int key_size, message_size;
    unsigned char *key = critical_array_to_c(env, j_key, &key_size);
    unsigned char *message = critical_array_to_c(env, j_message, &message_size);

    hmac_sha512(key, key_size, message, message_size, digest);

    critical_array_release(env, j_key, key);
    critical_array_release(env, j_message, message);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA512_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA512_DIGEST_LENGTH, (jbyte *) digest);
    free(digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_blake2b(JNIEnv *env, jobject thiz, jbyteArray input_jbyteArray, jint outlen_jint) {
    int input_len;
    unsigned char *input = critical_array_to_c(env, input_jbyteArray, &input_len);

    uint8_t *output = (uint8_t *) calloc(outlen_jint, sizeof(uint8_t));

    if (blake2b((uint8_t *) input, input_len, output, outlen_jint) != 0) {
        critical_array_release(env, input_jbyteArray, input);
        free(output);
        return NULL;
    }

    critical_array_release(env, input_jbyteArray, input);

    jbyteArray result = (*env)->NewByteArray(env, outlen_jint);
    if (result == NULL) {
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, result, 0, outlen_jint, (jbyte *) output);
    free(output);
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_blake2bWithKey(JNIEnv *env, jobject thiz, jbyteArray input_jbyteArray, jbyteArray key_jbyteArray,
                                                           jint outlen_jint) {
    int input_len, key_len;
    unsigned char *input = critical_array_to_c(env, input_jbyteArray, &input_len);
    unsigned char *key = critical_array_to_c(env, key_jbyteArray, &key_len);

    uint8_t *output = (uint8_t *) calloc(outlen_jint, sizeof(uint8_t));

    if (blake2b_Key((uint8_t *) input, input_len, key, key_len, output, outlen_jint) != 0) {
        critical_array_release(env, input_jbyteArray, input);
        critical_array_release(env, key_jbyteArray, key);
        free(output);
        return NULL;
    }

    critical_array_release(env, input_jbyteArray, input);
    critical_array_release(env, key_jbyteArray, key);

    jbyteArray result = (*env)->NewByteArray(env, outlen_jint);
    if (result == NULL) {
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, result, 0, outlen_jint, (jbyte *) output);
    free(output);
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_blake2s(JNIEnv *env, jobject thiz, jbyteArray input_jbyteArray, jint outlen_jint) {
    int input_len;
    unsigned char *input = critical_array_to_c(env, input_jbyteArray, &input_len);

    uint8_t *output = (uint8_t *) calloc(outlen_jint, sizeof(uint8_t));

    if (blake2s((uint8_t *) input, input_len, output, outlen_jint) != 0) {
        copy_array_release(env, input_jbyteArray, input);
        free(output);
        return NULL;
    }

    critical_array_release(env, input_jbyteArray, input);

    jbyteArray result = (*env)->NewByteArray(env, outlen_jint);
    if (result == NULL) {
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, result, 0, outlen_jint, (jbyte *) output);
    free(output);
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_blake2sWithKey(JNIEnv *env, jobject thiz, jbyteArray input_jbyteArray, jbyteArray key_jbyteArray,
                                                           jint outlen_jint) {
    int input_len, key_len;
    unsigned char *input = critical_array_to_c(env, input_jbyteArray, &input_len);
    unsigned char *key = critical_array_to_c(env, key_jbyteArray, &key_len);

    uint8_t *output = (uint8_t *) calloc(outlen_jint, sizeof(uint8_t));

    if (blake2s_Key((uint8_t *) input, input_len, key, key_len, output, outlen_jint) != 0) {
        critical_array_release(env, input_jbyteArray, input);
        critical_array_release(env, key_jbyteArray, key);
        free(output);
        return NULL;
    }

    critical_array_release(env, input_jbyteArray, input);
    critical_array_release(env, key_jbyteArray, key);

    jbyteArray result = (*env)->NewByteArray(env, outlen_jint);
    if (result == NULL) {
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, result, 0, outlen_jint, (jbyte *) output);
    free(output);
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_blake_1256(JNIEnv *env, jobject thiz, jbyteArray input_jbyteArray) {
    int input_len;
    unsigned char *input = critical_array_to_c(env, input_jbyteArray, &input_len);

    uint8_t *output = (uint8_t *) calloc(BLAKE256_DIGEST_LENGTH, sizeof(uint8_t));

    blake256((uint8_t *) input, input_len, output);

    critical_array_release(env, input_jbyteArray, input);

    jbyteArray result = (*env)->NewByteArray(env, BLAKE256_DIGEST_LENGTH);
    if (result == NULL) {
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, result, 0, BLAKE256_DIGEST_LENGTH, (jbyte *) output);
    free(output);
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_sig_1to_1der(JNIEnv *env, jobject thiz, jbyteArray j_sign) {
    uint8_t *der = (uint8_t *) calloc(72, sizeof(uint8_t));
    if (!der) {
        return NULL;
    }

    int sign_size;
    unsigned char *sign = critical_array_to_c(env, j_sign, &sign_size);

    int der_len = sig_to_der(sign, der);
    critical_array_release(env, j_sign, sign);

    jbyteArray returnBytes = (*env)->NewByteArray(env, der_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, der_len, (jbyte *) der);
    free(der);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_der_1to_1sig(JNIEnv *env, jobject thiz, jbyteArray j_der) {
    uint8_t *sign = (uint8_t *) calloc(64, sizeof(uint8_t));
    if (!sign) {
        return NULL;
    }

    int der_size;
    unsigned char *der = critical_array_to_c(env, j_der, &der_size);

    int status = der_to_sig(der, sign);
    critical_array_release(env, j_der, der);

    if (status == -1) {
        free(sign);
        return NULL;
    }

    jbyteArray returnBytes = (*env)->NewByteArray(env, 64);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, 64, (jbyte *) sign);
    free(sign);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_pbkdf2_1hmac_1sha256(JNIEnv *env, jclass cls, jbyteArray passArray, jbyteArray saltArray, jint iterations,
                                                                 jint byte_count) {
    int pass_size, salt_size;
    uint8_t *pass = copy_array_to_c(env, passArray, &pass_size);
    uint8_t *salt = copy_array_to_c(env, saltArray, &salt_size);
    if (!pass || !salt) {
        return NULL;
    }

    uint8_t *output = (uint8_t *) calloc(byte_count, sizeof(uint8_t));
    if (!output) {
        copy_array_release(env, passArray, pass);
        copy_array_release(env, saltArray, salt);
        return NULL;
    }

    pbkdf2_hmac_sha256(pass, pass_size, salt, salt_size, iterations, output, byte_count);

    // Release the arrays
    copy_array_release(env, passArray, pass);
    copy_array_release(env, saltArray, salt);

    // Convert C array to Java byte array
    jbyteArray resultArray = (*env)->NewByteArray(env, byte_count);
    if (!resultArray) {
        free(output);
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, resultArray, 0, byte_count, (jbyte *) output);

    free(output);
    return resultArray;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_core_jni_CryptoJNI_pbkdf2_1hmac_1sha512(JNIEnv *env, jclass cls, jbyteArray passArray, jbyteArray saltArray, jint iterations,
                                                                 jint byte_count) {
    int pass_size, salt_size;
    uint8_t *pass = copy_array_to_c(env, passArray, &pass_size);
    uint8_t *salt = copy_array_to_c(env, saltArray, &salt_size);
    if (!pass || !salt) {
        return NULL;
    }

    uint8_t *output = (uint8_t *) calloc(byte_count, sizeof(uint8_t));
    if (!output) {
        copy_array_release(env, passArray, pass);
        copy_array_release(env, saltArray, salt);
        return NULL;
    }
    pbkdf2_hmac_sha512(pass, pass_size, salt, salt_size, iterations, output, byte_count);

    // Release the arrays
    copy_array_release(env, passArray, pass);
    copy_array_release(env, saltArray, salt);

    // Convert C array to Java byte array
    jbyteArray resultArray = (*env)->NewByteArray(env, byte_count);
    if (!resultArray) {
        free(output);
        return NULL;
    }
    (*env)->SetByteArrayRegion(env, resultArray, 0, byte_count, (jbyte *) output);

    free(output);
    return resultArray;
}