//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>

#include "base58.h"
#include "sha2.h"
#include <string.h>

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

    char out[64];
    size_t out_len = 64;
    size_t bytesSize = data_size_jint;
    if (base58_encode(bytes, bytesSize, out, &out_len) == -1) {
        return (*env)->NewStringUTF(env, NULL);
    }
    (*env)->ReleaseByteArrayElements(env, bytes_jbyteArray, bytes, 0);
    return (*env)->NewStringUTF(env, out);
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
Java_com_smallraw_chain_lib_jni_CryptoJNI_doubleSha256(JNIEnv *env, jobject thiz, jbyteArray date_jbyteArray,
                                                       jint data_size) {
    const unsigned char *date = (*env)->GetByteArrayElements(env, date_jbyteArray, 0);
    unsigned char digest[SHA256_DIGEST_SIZE];
    sha256(date, data_size, digest);
    sha256(digest, data_size, digest);
    (*env)->ReleaseByteArrayElements(env, date_jbyteArray, date, 0);

    jbyteArray returnBytes = (*env)->NewByteArray(env, SHA256_DIGEST_SIZE);
    (*env)->SetByteArrayRegion(env, returnBytes, 0, SHA256_DIGEST_SIZE, (jbyte *) digest);
    return returnBytes;
}