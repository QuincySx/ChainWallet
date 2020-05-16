//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>
#include "include/secp256k1.h"
#include <string.h>
#include <stdlib.h>

#define PUBLIC_KEY_SIZE 65

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_00024Companion_createPublicKey(JNIEnv *env,
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
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_00024Companion_sign(JNIEnv *env,
                                                                 jobject byteObj /* this */,
                                                                 jbyteArray private_key_jbytearray,
                                                                 jbyteArray message_jbytearray) {
    const unsigned char *privateKey = (const unsigned char *) (*env)->GetByteArrayElements(
            env, private_key_jbytearray, 0);

    const unsigned char *messages = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                         message_jbytearray,
                                                                                         0);

    uint8_t sig[64], pby;
    int ret = secp256k1_sign(privateKey, messages, sig, &pby);
    if (ret != 0) {
        // Failed to sign.
        return 0;
    }

    jbyteArray outputBytes = (*env)->NewByteArray(env, 64);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, 64, (jbyte *) sig);

    return outputBytes;
}

JNIEXPORT jboolean JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_00024Companion_verify(JNIEnv *env,
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

//    const int pubKeySize = (*env)->GetArrayLength(env, public_key_jbytearray);
//    const int signatureSize = (*env)->GetArrayLength(env, signature_jbytearray);
//    const int messageSize = (*env)->GetArrayLength(env, message_jbytearray);

    int ret = secp256k1_verify(publicKey, signature, message);

//    if (pubKeySize != PUBLIC_KEY_SIZE) {
//        return JNI_FALSE;
//    }
//    if (signatureSize != 72) {
//        return JNI_FALSE;
//    }

    return ret == 0;
}
