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

JNIEXPORT jstring JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_hexToStr(JNIEnv *env,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_strToHex(JNIEnv *env, jobject thiz,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_base58Encode(JNIEnv *env,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_base58Decode(JNIEnv *env, jobject thiz,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_base58EncodeCheck(JNIEnv *env,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_base58DecodeCheck(JNIEnv *env,
                                                                           jobject thiz,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_sha256(JNIEnv *env, jobject thiz,
                                                                jbyteArray date_jbyteArray,
                                                                jint data_size) {
    const unsigned char *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    uint8_t digest[SHA256_DIGEST_LENGTH];
    sha256_Raw(date, data_size, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_doubleSha256(JNIEnv *env, jobject thiz,
                                                                      jbyteArray date_jbyteArray,
                                                                      jint data_size) {
    const unsigned char *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    char digest[SHA256_DIGEST_LENGTH];
    sha256_Raw(date, data_size, digest);
    sha256_Raw(digest, SHA256_DIGEST_LENGTH, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_ripemd160(JNIEnv *env, jobject thiz,
                                                                   jbyteArray
                                                                   date_jbyteArray,
                                                                   jint data_size) {
    const uint8_t *message = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);

    uint8_t output[RIPEMD160_DIGEST_LENGTH] = {0};
    ripemd160(message, data_size, output);

    jbyteArray returnBytes = (*env)->NewByteArray(env, RIPEMD160_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, RIPEMD160_DIGEST_LENGTH, (jbyte *) &output);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_sha3_1256(JNIEnv *env, jobject thiz,
                                                                   jbyteArray date_jbyteArray,
                                                                   jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    uint8_t *digest = malloc(SHA3_256_DIGEST_LENGTH);

    sha3_256(date, data_size, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_doubleSha3_1256(JNIEnv *env, jobject thiz,
                                                                         jbyteArray date_jbyteArray,
                                                                         jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    uint8_t *digest = malloc(SHA3_256_DIGEST_LENGTH);

    sha3_256(date, data_size, digest);
    sha3_256(digest, SHA3_256_DIGEST_LENGTH, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_keccak_1256(JNIEnv *env, jobject thiz,
                                                                     jbyteArray date_jbyteArray,
                                                                     jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    uint8_t *digest = malloc(SHA3_256_DIGEST_LENGTH);

    keccak_256(date, data_size, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_doubleKeccak_1256(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jbyteArray date_jbyteArray,
                                                                           jint data_size) {
    uint8_t *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    uint8_t *digest = malloc(SHA3_256_DIGEST_LENGTH);

    keccak_256(date, data_size, digest);
    keccak_256(digest, SHA3_256_DIGEST_LENGTH, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA3_256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA3_256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_hmac_1sha256(JNIEnv *env, jobject thiz,
                                                                      jbyteArray key_jbyteArray,
                                                                      jbyteArray message_jbyteArray,
                                                                      jint key_size,
                                                                      jint message_size) {
    const unsigned char *key = (*env)->GetByteArrayElements(env, key_jbyteArray, 0);
    const unsigned char *message = (*env)->GetByteArrayElements(env, message_jbyteArray, 0);

    uint8_t digest[SHA256_DIGEST_LENGTH];
    hmac_sha256(key, key_size, message, message_size, digest);

    (*env)->ReleaseByteArrayElements(env, key_jbyteArray, key, 0);
    (*env)->ReleaseByteArrayElements(env, message_jbyteArray, message, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_hmac_1sha512(JNIEnv *env, jobject thiz,
                                                                      jbyteArray key_jbyteArray,
                                                                      jbyteArray message_jbyteArray,
                                                                      jint key_size,
                                                                      jint message_size) {
    const unsigned char *key = (*env)->GetByteArrayElements(env, key_jbyteArray, 0);
    const unsigned char *message = (*env)->GetByteArrayElements(env, message_jbyteArray, 0);

    uint8_t digest[SHA512_DIGEST_LENGTH];
    hmac_sha512(key, key_size, message, message_size, digest);

    (*env)->ReleaseByteArrayElements(env, key_jbyteArray, key, 0);
    (*env)->ReleaseByteArrayElements(env, message_jbyteArray, message, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA512_DIGEST_LENGTH);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA512_DIGEST_LENGTH, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_sig_1to_1der(JNIEnv *env, jobject thiz,
                                                                      jbyteArray sign_jbyteArray,
                                                                      jint sign_size) {
    const jbyte *sign = (*env)->GetByteArrayElements(env, sign_jbyteArray, 0);

    uint8_t der[72];

    sig_to_der(sign, der);

    (*env)->ReleaseByteArrayElements(env, sign_jbyteArray, sign, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, 72);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, 72, (jbyte *) der);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_der_1to_1sig(JNIEnv *env, jobject thiz,
                                                                      jbyteArray der_jbyteArray,
                                                                      jint der_size) {
    const jbyte *der = (*env)->GetByteArrayElements(env, der_jbyteArray, 0);

    uint8_t sign[64];

    der_to_sig(der, sign);

    (*env)->ReleaseByteArrayElements(env, der_jbyteArray, der, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, 64);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, 64, (jbyte *) sign);
    return returnBytes;
}
