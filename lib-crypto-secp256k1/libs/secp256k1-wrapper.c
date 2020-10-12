//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>
#include "include/secp256k1.h"
#include <string.h>
#include <stdlib.h>

void padding_list(unsigned char *in, const int inLen, unsigned char *out) {
    memcpy(out + 32 - inLen, in, inLen);
//    memcpy(out, in, inLen);
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_createPublicKey(JNIEnv *env,
                                                                            jobject byteObj /* this */,
                                                                            jbyteArray privKeyBytes_jbyteArray,
                                                                            jboolean compressed_jbool) {
    unsigned char *privateKey = (unsigned char *) (*env)->GetByteArrayElements(env,
                                                                               privKeyBytes_jbyteArray,
                                                                               0);
    int compressed = compressed_jbool == JNI_TRUE;

    uint8_t *pbKey;
    int size;
    if (compressed) {
        size = 33;
        pbKey = malloc(33);
    } else {
        size = 65;
        pbKey = malloc(65);
    }
    secp256k1_get_public(privateKey, pbKey, compressed);

    jbyteArray nullBytes = (*env)->NewByteArray(env, size);
    (*env)->SetByteArrayRegion(env, nullBytes, 0, size, (jbyte *) pbKey);

    return nullBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_sign(JNIEnv *env,
                                                                 jobject byteObj /* this */,
                                                                 jbyteArray private_key_jbytearray,
                                                                 jbyteArray message_jbytearray,
                                                                 jint message_size) {
    const unsigned char *privateKey = (const unsigned char *) (*env)->GetByteArrayElements(
            env, private_key_jbytearray, 0);

    const unsigned char *messages = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                         message_jbytearray,
                                                                                         0);
    if (message_size > 32) {
        return 0;
    }
    uint8_t sig[64], pby;
    int ret;
    if (message_size == 32) {
        ret = secp256k1_sign(privateKey, messages, sig, &pby);
    } else {
        unsigned char digest[32] = {0};
        padding_list(messages, message_size, &digest);
        ret = secp256k1_sign(privateKey, digest, sig, &pby);
    }

    if (ret != 0) {
        // Failed to sign.
        return 0;
    }

    jbyteArray outputBytes = (*env)->NewByteArray(env, 64);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, 64, (jbyte *) sig);

    return outputBytes;
}

JNIEXPORT jboolean JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_verify(JNIEnv *env,
                                                                   jobject byteObj /* this */,
                                                                   jbyteArray public_key_jbytearray,
                                                                   jbyteArray signature_jbytearray,
                                                                   jbyteArray message_jbytearray,
                                                                   jint message_size) {
    const unsigned char *publicKey = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                          public_key_jbytearray,
                                                                                          0);

    const unsigned char *signature = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                          signature_jbytearray,
                                                                                          0);

    const unsigned char *message = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                        message_jbytearray,
                                                                                        0);
    int ret;
    if (message_size == 32) {
        ret = secp256k1_verify(publicKey, signature, message);
    } else {
        unsigned char digest[32] = {0};
        padding_list(message, message_size, &digest);
        ret = secp256k1_verify(publicKey, signature, digest);
    }

    return ret == 0;
}
