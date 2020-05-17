//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>

#include <string.h>
#include "include/base58.h"
#include "include/sha2.h"
#include "include/hmac_sha2.h"
#include "include/ripemd160.h"
#include "include/hexstring.h"
#include "src/keccak.c"

JNIEXPORT jstring JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_hexToStr(JNIEnv *env,
                                                   jobject byteObj /* this */,
                                                   jbyteArray bytes_jbyteArray,
                                                   jint data_size_jint) {
    unsigned char *bytes = (*env)->GetByteArrayElements(env, bytes_jbyteArray, 0);
    int bytesSize = data_size_jint;

    int out_len = bytesSize * 2;
    char *out = malloc(out_len);

    hexToStr(out, bytes, bytesSize, 1);

    (*env)->ReleaseByteArrayElements(env, bytes_jbyteArray, bytes, 0);

    return (*env)->NewStringUTF(env, out);
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_strToHex(JNIEnv *env, jobject thiz,
                                                   jstring date_jbyteArray) {
    char *bytes = (const char *) (*env)->GetStringUTFChars(env, date_jbyteArray, 0);
    int bytesSize = (*env)->GetStringUTFLength(env, date_jbyteArray);

    int out_len = bytesSize / 2;
    char *out = malloc(out_len);
    strToHex(out, bytes, out_len);

    (*env)->ReleaseStringUTFChars(env, date_jbyteArray, (jchar *) bytes);

    jbyteArray returnBytes = (*env)->NewByteArray(env, out_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, out_len, (jbyte *) out);
    return returnBytes;
}

/**
 * @brief creates public key from given bytes(private key) and returns it in uncompressed form
 * @param env
 * @param byteObj
 * @param privKeyBytes
 * @return public key byte[]
 */
JNIEXPORT jstring JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_base58Encode(JNIEnv *env,
                                                       jobject byteObj /* this */,
                                                       jbyteArray bytes_jbyteArray,
                                                       jint data_size_jint) {
    const char *bytes = (*env)->GetByteArrayElements(env, bytes_jbyteArray, 0);
    int bytesSize = data_size_jint;

    int out_len;

    unsigned char *out = base58_encode(bytes, bytesSize, &out_len);
    (*env)->ReleaseByteArrayElements(env, bytes_jbyteArray, bytes, 0);

    return (*env)->NewStringUTF(env, out);
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_base58Decode(JNIEnv *env, jobject thiz,
                                                       jstring date_jbyteArray) {
    const char *bytes = (const char *) (*env)->GetStringUTFChars(env, date_jbyteArray, 0);
    int bytesSize = (*env)->GetStringUTFLength(env, date_jbyteArray);

    int out_len;
    unsigned char *out = base58_decode(bytes, bytesSize, &out_len);

    (*env)->ReleaseStringUTFChars(env, date_jbyteArray, (jchar *) bytes);

    jbyteArray returnBytes = (*env)->NewByteArray(env, out_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, out_len, (jbyte *) out);
    return returnBytes;
}

JNIEXPORT jstring JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_base58EncodeCheck(JNIEnv *env,
                                                            jobject byteObj /* this */,
                                                            jbyteArray bytes_jbyteArray,
                                                            jint data_size_jint) {
    const char *bytes = (*env)->GetByteArrayElements(env, bytes_jbyteArray, 0);
    int bytesSize = data_size_jint;

    int out_len;

    unsigned char *out = base58_encode_check(bytes, bytesSize, &out_len);
    (*env)->ReleaseByteArrayElements(env, bytes_jbyteArray, bytes, 0);

    return (*env)->NewStringUTF(env, out);
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_base58DecodeCheck(JNIEnv *env, jobject thiz,
                                                            jstring date_jbyteArray) {
    const char *bytes = (const char *) (*env)->GetStringUTFChars(env, date_jbyteArray, 0);
    int bytesSize = (*env)->GetStringUTFLength(env, date_jbyteArray);

    int out_len;
    unsigned char *out = base58_decode_check(bytes, bytesSize, &out_len);

    (*env)->ReleaseStringUTFChars(env, date_jbyteArray, (jchar *) bytes);

    jbyteArray returnBytes = (*env)->NewByteArray(env, out_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, out_len, (jbyte *) out);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_sha256(JNIEnv *env, jobject thiz,
                                                 jbyteArray date_jbyteArray,
                                                 jint data_size) {
    const unsigned char *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    unsigned char digest[SHA256_DIGEST_SIZE];
    sha256(date, data_size, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_SIZE);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_SIZE, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_doubleSha256(JNIEnv *env, jobject thiz,
                                                       jbyteArray date_jbyteArray, jint data_size) {
    const unsigned char *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    unsigned char digest[SHA256_DIGEST_SIZE];
    sha256(date, data_size, digest);
    sha256(digest, SHA256_DIGEST_SIZE, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_SIZE);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_SIZE, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_ripemd160(JNIEnv *env, jobject thiz, jbyteArray
date_jbyteArray, jint data_size) {
    const uint8_t *message = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);

    uint8_t output[RIPEMD160_DIGEST_LENGTH] = {0};
    ripemd160(message, data_size, output);

    jbyteArray returnBytes = (*env)->NewByteArray(env, 20);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, 20, (jbyte *) &output);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_sha3_1256(JNIEnv *env, jobject thiz,
                                                    jbyteArray date_jbyteArray,
                                                    jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    int sha3_256_len = 32;
    uint8_t *digest = malloc(sha3_256_len);

    sha3_256(digest, sha3_256_len, date, data_size);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, sha3_256_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, sha3_256_len, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_doubleSha3_1256(JNIEnv *env, jobject thiz,
                                                          jbyteArray date_jbyteArray,
                                                          jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    int sha3_256_len = 32;
    uint8_t *digest = malloc(sha3_256_len);

    sha3_256(digest, sha3_256_len, date, data_size);
    sha3_256(digest, sha3_256_len, digest, sha3_256_len);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, sha3_256_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, sha3_256_len, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_keccak_1256(JNIEnv *env, jobject thiz,
                                                      jbyteArray date_jbyteArray,
                                                      jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    int keccak_256_len = 32;
    uint8_t *digest = malloc(keccak_256_len);

    keccak_256(digest, keccak_256_len, date, data_size);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, keccak_256_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, keccak_256_len, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_doubleKeccak_1256(JNIEnv *env, jobject thiz,
                                                            jbyteArray date_jbyteArray,
                                                            jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    int keccak_256_len = 32;
    uint8_t *digest = malloc(keccak_256_len);

    keccak_256(digest, keccak_256_len, date, data_size);
    keccak_256(digest, keccak_256_len, digest, keccak_256_len);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, keccak_256_len);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, keccak_256_len, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_hmac_1sha256(JNIEnv *env, jobject thiz,
                                                       jbyteArray key_jbyteArray,
                                                       jbyteArray message_jbyteArray,
                                                       jint key_size,
                                                       jint message_size) {
    const unsigned char *key = (*env)->GetByteArrayElements(env, key_jbyteArray, 0);
    const unsigned char *message = (*env)->GetByteArrayElements(env, message_jbyteArray, 0);

    uint8_t *digest = malloc(SHA256_DIGEST_SIZE);

    hmac_sha256(key, key_size, message, message_size, digest, SHA256_DIGEST_SIZE);

    (*env)->ReleaseByteArrayElements(env, key_jbyteArray, key, 0);
    (*env)->ReleaseByteArrayElements(env, message_jbyteArray, message, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_SIZE);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_SIZE, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_00024Companion_hmac_1sha512(JNIEnv *env, jobject thiz,
                                                       jbyteArray key_jbyteArray,
                                                       jbyteArray message_jbyteArray,
                                                       jint key_size,
                                                       jint message_size) {
    const unsigned char *key = (*env)->GetByteArrayElements(env, key_jbyteArray, 0);
    const unsigned char *message = (*env)->GetByteArrayElements(env, message_jbyteArray, 0);

    uint8_t *digest = malloc(SHA512_DIGEST_SIZE);

    hmac_sha512(key, key_size, message, message_size, digest, SHA512_DIGEST_SIZE);

    (*env)->ReleaseByteArrayElements(env, key_jbyteArray, key, 0);
    (*env)->ReleaseByteArrayElements(env, message_jbyteArray, message, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA512_DIGEST_SIZE);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA512_DIGEST_SIZE, (jbyte *) digest);
    return returnBytes;
}