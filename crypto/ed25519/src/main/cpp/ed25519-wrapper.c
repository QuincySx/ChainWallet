//
// Created by q7728 on 2020/5/18.
//
#include <jni.h>
#include "ed25519-donna/ed25519.h"
#include "include/jni_method.h"

#define PUBLIC_KEY_SIZE 32
#define SIGNATURE_SIZE 64

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Ed25519JNI_createPublicKey(JNIEnv *env, jobject byteObj /* this */, jbyteArray jPrivKeyBytes) {
    int key_len;
    unsigned char *private_key = critical_array_to_c(env, jPrivKeyBytes, &key_len);

    ed25519_public_key public_key = {0};
    ed25519_publickey(private_key, public_key);
    critical_array_release(env, jPrivKeyBytes, private_key);

    jbyteArray outputBytes = (*env)->NewByteArray(env, PUBLIC_KEY_SIZE);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, PUBLIC_KEY_SIZE, (jbyte *) public_key);

    return outputBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Ed25519JNI_sign(JNIEnv *env, jobject byteObj, jbyteArray jPrivateKey, jbyteArray jMessage, jint jMessageSize) {
    int key_len, msg_len;
    unsigned char *private_key = copy_array_to_c(env, jPrivateKey, &key_len);
    unsigned char *messages = copy_array_to_c(env, jMessage, &msg_len);

    ed25519_signature sig = {0};
    ed25519_sign(messages, jMessageSize, private_key, sig);

    jbyteArray outputBytes = (*env)->NewByteArray(env, SIGNATURE_SIZE);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, SIGNATURE_SIZE, (jbyte *) sig);

    // Cleanup
    copy_array_release(env, jPrivateKey, private_key);
    copy_array_release(env, jMessage, messages);

    return outputBytes;
}

JNIEXPORT jboolean JNICALL
Java_com_smallraw_crypto_jni_Ed25519JNI_verify(JNIEnv *env, jobject byteObj, jbyteArray jPublicKey, jbyteArray jSignature, jbyteArray jMessage,
                                               jint jMessageSize) {
    int key_len, msg_len, sig_len;
    unsigned char *public_key = copy_array_to_c(env, jPublicKey, &key_len);
    unsigned char *signature = copy_array_to_c(env, jSignature, &sig_len);
    unsigned char *message = copy_array_to_c(env, jMessage, &msg_len);

    int result = ed25519_sign_open(message, jMessageSize, public_key, signature);

    // Cleanup
    copy_array_release(env, jPublicKey, public_key);
    copy_array_release(env, jSignature, signature);
    copy_array_release(env, jMessage, message);

    return result == 0 ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Ed25519JNI_curve25519CreatePublicKey(JNIEnv *env, jobject byteObj /* this */, jbyteArray jPrivateKey) {
    int key_len;
    unsigned char *private_key = critical_array_to_c(env, jPrivateKey, &key_len);

    curve25519_key pk = {0};
    curve25519_scalarmult_basepoint(pk, private_key);
    critical_array_release(env, jPrivateKey, private_key);

    jbyteArray outputBytes = (*env)->NewByteArray(env, PUBLIC_KEY_SIZE);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, PUBLIC_KEY_SIZE, (jbyte *) pk);
    return outputBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Ed25519JNI_curve25519CreateSharedKey(JNIEnv *env, jobject byteObj, jbyteArray jLocalPriKey, jbyteArray jRemotePubKey) {
    int prikey_len, pubkey_len;
    unsigned char *privateKey = critical_array_to_c(env, jLocalPriKey, &prikey_len);
    unsigned char *publicKey = critical_array_to_c(env, jRemotePubKey, &pubkey_len);

    curve25519_key shared = {0};
    curve25519_scalarmult(shared, privateKey, publicKey);
    critical_array_release(env, jLocalPriKey, privateKey);
    critical_array_release(env, jRemotePubKey, publicKey);

    jbyteArray outputBytes = (*env)->NewByteArray(env, PUBLIC_KEY_SIZE);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, PUBLIC_KEY_SIZE, (jbyte *) shared);

    return outputBytes;
}