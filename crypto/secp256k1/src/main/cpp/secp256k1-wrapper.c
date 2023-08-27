//
// Created by QuincySx on 5/12/20.
//

#include <jni.h>
#include "include/secp256k1.h"
#include "include/jni_method.h"
#include <string.h>
#include <stdlib.h>

#define COMPRESSED_PUBLIC_KEY_SIZE 33
#define UNCOMPRESSED_PUBLIC_KEY_SIZE 65
#define SIGNATURE_SIZE 64
#define MAX_MESSAGE_SIZE 32

void padding_list(const unsigned char *in, const int inLen, unsigned char *out) {
    if (inLen > MAX_MESSAGE_SIZE) {
        return; // Handle this case appropriately.
    }
    memcpy(out + MAX_MESSAGE_SIZE - inLen, in, inLen);
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_createPublicKey(JNIEnv *env, jobject byteObj, jbyteArray jPrivKeyBytes, jboolean jCompressed) {
    int keySize;
    unsigned char *privateKey = critical_array_to_c(env, jPrivKeyBytes, &keySize);
    int compressed = jCompressed == JNI_TRUE;

    int size = compressed ? COMPRESSED_PUBLIC_KEY_SIZE : UNCOMPRESSED_PUBLIC_KEY_SIZE;
    uint8_t *pbKey = (uint8_t *) calloc(size, sizeof(uint8_t));
    if (!pbKey) {
        critical_array_release(env, jPrivKeyBytes, privateKey);
        return NULL;
    }
    secp256k1_get_public(privateKey, pbKey, compressed);
    critical_array_release(env, jPrivKeyBytes, privateKey);

    jbyteArray outputBytes = (*env)->NewByteArray(env, size);
    (*env)->SetByteArrayRegion(env, outputBytes, 0, size, (jbyte *) pbKey);

    free(pbKey);
    return outputBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_sign(JNIEnv *env, jobject byteObj, jbyteArray j_private_key, jbyteArray j_message) {
    int key_size, message_size;
    unsigned char *messages = copy_array_to_c(env, j_message, &message_size);

    if (message_size > MAX_MESSAGE_SIZE) {
        copy_array_release(env, j_message, messages);
        return NULL;
    }

    unsigned char *private_key = copy_array_to_c(env, j_private_key, &key_size);

    uint8_t pby, *sig = (uint8_t *) calloc(SIGNATURE_SIZE, sizeof(uint8_t));
    int ret;
    if (message_size == MAX_MESSAGE_SIZE) {
        ret = secp256k1_sign(private_key, messages, sig, &pby);
    } else {
        unsigned char digest[MAX_MESSAGE_SIZE] = {0};
        padding_list(messages, message_size, digest);
        ret = secp256k1_sign(private_key, digest, sig, &pby);
    }

    if (ret != 0) {
        // Failed to sign.
        copy_array_release(env, j_private_key, private_key);
        copy_array_release(env, j_message, messages);
        return NULL;
    }

    jbyteArray output_bytes = (*env)->NewByteArray(env, SIGNATURE_SIZE);
    (*env)->SetByteArrayRegion(env, output_bytes, 0, SIGNATURE_SIZE, (jbyte *) sig);

    // Cleanup
    copy_array_release(env, j_private_key, private_key);
    copy_array_release(env, j_message, messages);

    return output_bytes;
}

JNIEXPORT jobjectArray JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_ethSign(JNIEnv *env, jobject byteObj, jbyteArray j_private_key, jbyteArray j_message,
                                                  jboolean j_compressed) {
    int key_size, message_size;
    unsigned char *messages = copy_array_to_c(env, j_message, &message_size);

    if (message_size > MAX_MESSAGE_SIZE) {
        copy_array_release(env, j_message, messages);
        return NULL;
    }

    unsigned char *private_key = copy_array_to_c(env, j_private_key, &key_size);

    int compressed = j_compressed == JNI_TRUE;
    uint8_t pby, *sig = (uint8_t *) calloc(SIGNATURE_SIZE, sizeof(uint8_t));
    int ret;
    if (message_size == MAX_MESSAGE_SIZE) {
        ret = secp256k1_eth_sign(private_key, messages, sig, &pby);
    } else {
        unsigned char digest[MAX_MESSAGE_SIZE] = {0};
        padding_list(messages, message_size, digest);
        ret = secp256k1_sign(private_key, digest, sig, &pby);
    }

    if (ret != 0) {
        // Failed to sign.
        copy_array_release(env, j_private_key, private_key);
        copy_array_release(env, j_message, messages);
        return NULL;
    }

    jbyteArray sign_array = (*env)->NewByteArray(env, SIGNATURE_SIZE);
    (*env)->SetByteArrayRegion(env, sign_array, 0, SIGNATURE_SIZE, (jbyte *) sig);

    jclass clazz = (*env)->GetObjectClass(env, sign_array);
    jobjectArray output_list = (*env)->NewObjectArray(env, 2, clazz, NULL);
    (*env)->SetObjectArrayElement(env, output_list, 0, sign_array);

    jbyte buf[1];
    buf[0] = 27 + pby + compressed * 4;
    jbyteArray recs = (*env)->NewByteArray(env, 1);
    (*env)->SetByteArrayRegion(env, recs, 0, 1, buf);

    (*env)->SetObjectArrayElement(env, output_list, 1, recs);

    // Cleanup
    copy_array_release(env, j_private_key, private_key);
    copy_array_release(env, j_message, messages);

    return output_list;
}

JNIEXPORT jboolean JNICALL
Java_com_smallraw_crypto_jni_Secp256k1JNI_verify(JNIEnv *env, jobject byteObj, jbyteArray j_public_key, jbyteArray j_signature,
                                                 jbyteArray j_message) {

    int key_size, message_size, signature_size;
    unsigned char *message = copy_array_to_c(env, j_message, &message_size);
    if (message_size > MAX_MESSAGE_SIZE) {
        return JNI_FALSE;
    }

    unsigned char *public_key = copy_array_to_c(env, j_public_key, &key_size);
    unsigned char *signature = copy_array_to_c(env, j_signature, &signature_size);

    int ret;
    if (message_size == MAX_MESSAGE_SIZE) {
        ret = secp256k1_verify(public_key, signature, message);
    } else {
        unsigned char digest[MAX_MESSAGE_SIZE] = {0};
        padding_list(message, message_size, digest);
        ret = secp256k1_verify(public_key, signature, digest);
    }

    // Cleanup
    copy_array_release(env, j_public_key, public_key);
    copy_array_release(env, j_signature, signature);
    copy_array_release(env, j_message, message);

    return ret == 0 ? JNI_TRUE : JNI_FALSE;
}
