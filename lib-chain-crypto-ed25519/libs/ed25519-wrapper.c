//
// Created by q7728 on 2020/5/18.
//
#include <jni.h>
#include "ed25519.h"

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Ed25519JNI_00024Companion_createPublicKey(JNIEnv *env,
                                                                          jobject byteObj /* this */,
                                                                          jbyteArray privKeyBytes_jbyteArray) {
    unsigned char *privateKey = (unsigned char *) (*env)->GetByteArrayElements(env,
                                                                               privKeyBytes_jbyteArray,
                                                                               0);
    ed25519_public_key publicKey;
    ed25519_publickey(privateKey, publicKey);

    jbyteArray outputBytes = (*env)->NewByteArray(env, 32);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, 32, (jbyte *) publicKey);

    return outputBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Ed25519JNI_00024Companion_sign(JNIEnv *env,
                                                               jobject byteObj /* this */,
                                                               jbyteArray private_key_jbytearray,
                                                               jbyteArray message_jbytearray,
                                                               jint message_size) {
    const unsigned char *privateKey = (const unsigned char *) (*env)->GetByteArrayElements(
            env, private_key_jbytearray, 0);

    const unsigned char *messages = (const unsigned char *) (*env)->GetByteArrayElements(env,
                                                                                         message_jbytearray,
                                                                                         0);

    ed25519_public_key publicKey;
    ed25519_publickey(privateKey, publicKey);

    ed25519_signature sig;
    ed25519_sign(messages, message_size, privateKey, publicKey, sig);

    jbyteArray outputBytes = (*env)->NewByteArray(env, 64);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, 64, (jbyte *) sig);

    return outputBytes;
}

JNIEXPORT jboolean JNICALL
Java_com_smallraw_chain_lib_jni_Ed25519JNI_00024Companion_verify(JNIEnv *env,
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
    return ed25519_sign_open(message, message_size, publicKey, signature) == 0;
}