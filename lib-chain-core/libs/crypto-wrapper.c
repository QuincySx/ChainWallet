//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>

#include "base58.h"
#include "sha2.h"
#include <string.h>
#include <ripemd160.h>
#include "ripemd160.h"

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
Java_com_smallraw_chain_lib_jni_CryptoJNI_base58DecodeCheck(JNIEnv *env, jobject thiz,
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
    unsigned char digest[SHA256_DIGEST_SIZE];
    sha256(date, data_size, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_SIZE);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_SIZE, (jbyte *) digest);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_doubleSha256(JNIEnv *env, jobject thiz,
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_ripemd160(JNIEnv *env, jobject thiz, jbyteArray
date_jbyteArray, jint data_size) {
    const char *message = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);


    struct ripemd160 structripemd160;
    ripemd160(&structripemd160, message, data_size);

    jbyteArray returnBytes = (*env)->NewByteArray(env, 20);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, 20, (jbyte *) structripemd160.u.u8);
    return returnBytes;
}