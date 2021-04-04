#include <jni.h>
#include "include/pbkdf2.h"
#include <string.h>
#include <stdlib.h>

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_wallet_mnemonic_jni_Pbkdf2JNI_pbkdf2HmacSha256(JNIEnv *env,
                                                                 jobject thiz,
                                                                 jcharArray mnemonic_jcharArray,
                                                                 jbyteArray passphrase_jbyteArray,
                                                                 jint mnemonic_size,
                                                                 jint passphrase_size,
                                                                 jint iterations
) {
    const unsigned char *mnemonic = (const unsigned char *) (*env)->GetCharArrayElements(
            env, mnemonic_jcharArray, 0);

    const unsigned char *passphrase = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                           passphrase_jbyteArray,
                                                                                           0);

    const uint8_t seedSize = 32;
    PBKDF2_HMAC_SHA256_CTX pctx;
    uint8_t *seed = malloc(seedSize);

    pbkdf2_hmac_sha256_Init(&pctx, (const uint8_t *) mnemonic, mnemonic_size, passphrase,
                            passphrase_size, 1);

    pbkdf2_hmac_sha256_Update(&pctx, iterations);
    pbkdf2_hmac_sha256_Final(&pctx, seed);


    jbyteArray seedBytes = (*env)->NewByteArray(env, seedSize);
    (*env)->SetByteArrayRegion(env, seedBytes, 0, seedSize, (jbyte *) seed);
    return seedBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_wallet_mnemonic_jni_Pbkdf2JNI_pbkdf2HmacSha512(JNIEnv *env, jobject thiz,
                                                                 jbyteArray mnemonic_jbyteArray,
                                                                 jbyteArray passphrase_jbyteArray,
                                                                 jint mnemonic_size,
                                                                 jint passphrase_size,
                                                                 jint iterations) {

    const uint8_t seedSize = 64;
    static PBKDF2_HMAC_SHA512_CTX pctx;
    uint8_t *seed = malloc(seedSize);

    const unsigned char *mnemonic = (const unsigned char *) (*env)->GetByteArrayElements(
            env, mnemonic_jbyteArray, 0);

    const unsigned char *passphrase = (const unsigned char *) (*env)->GetByteArrayElements(
            env, passphrase_jbyteArray, 0);

    pbkdf2_hmac_sha512_Init(&pctx, mnemonic, mnemonic_size, passphrase,
                            passphrase_size, 1);
    pbkdf2_hmac_sha512_Update(&pctx, iterations);
    pbkdf2_hmac_sha512_Final(&pctx, seed);

    jbyteArray seedBytes = (*env)->NewByteArray(env, seedSize);
    (*env)->SetByteArrayRegion(env, seedBytes, 0, seedSize, (jbyte *) seed);
    return seedBytes;
}