//
// Created by q7728 on 2021/4/3.
//
#include <jni.h>
#include "aes/aes.h"
#include "include/jni_method.h"


JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_Aes_encrypt(JNIEnv *env, jobject thiz,
                                     jbyteArray key_jbyteArray,
                                     jbyteArray data_jbyteArray,
                                     jbyteArray iv_jbyteArray,
                                     jint mode_jint) {

    int keySize, dataSize, ivSize;
    unsigned char *key = as_unsigned_char_array(env, key_jbyteArray, &keySize);
    unsigned char *data = as_unsigned_char_array(env, data_jbyteArray, &dataSize);
    unsigned char *iv = as_unsigned_char_array(env, iv_jbyteArray, &ivSize);

    int len = dataSize;
    int mode = mode_jint;

    unsigned char obuf[dataSize];


    aes_init();
    aes_encrypt_ctx ctx = {0};
    int ret = aes_encrypt_key(key, keySize, &ctx);
    if (ret == EXIT_FAILURE) {
        return (*env)->NewByteArray(env, 0);
    }

    switch (mode) {
        case 0:
            // ECB
            ret = aes_ecb_encrypt(data, obuf, len, &ctx);
            break;
        case 1:
            // CBC
            ret = aes_cbc_encrypt(data, obuf, len, iv, &ctx);
            break;
        case 2:
            // CFB
            ret = aes_cfb_encrypt(data, obuf, len, iv, &ctx);
            break;
        case 3:
            // OFB
            ret = aes_ofb_encrypt(data, obuf, len, iv, &ctx);
            break;
        case 4:
            // CTR
            ret = aes_ctr_encrypt(data, obuf, len, iv, aes_ctr_cbuf_inc, &ctx);
            break;
        default:
            ret = EXIT_FAILURE;
            break;
    }

    if (ret == EXIT_FAILURE) {
        return (*env)->NewByteArray(env, 0);
    }

    jbyteArray returnBytes = as_byte_array(env, obuf, dataSize);

    release_jbyte_array(env, data_jbyteArray, data);
    release_jbyte_array(env, key_jbyteArray, key);
    release_jbyte_array(env, iv_jbyteArray, iv);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_Aes_decrypt(JNIEnv *env, jobject thiz,
                                     jbyteArray key_jbyteArray,
                                     jbyteArray data_jbyteArray,
                                     jbyteArray iv_jbyteArray,
                                     jint mode_jint) {
    int keySize, dataSize, ivSize;
    unsigned char *key = as_unsigned_char_array(env, key_jbyteArray, &keySize);
    unsigned char *data = as_unsigned_char_array(env, data_jbyteArray, &dataSize);
    unsigned char *iv = as_unsigned_char_array(env, iv_jbyteArray, &ivSize);

    int len = dataSize;
    int mode = mode_jint;

    unsigned char obuf[dataSize];

    aes_init();
    aes_encrypt_ctx ctx = {0};
    aes_decrypt_ctx deCtx = {0};
    int ret;
    if (mode == 0 || mode == 1) {
        ret = aes_decrypt_key(key, keySize, &deCtx);
    } else {
        ret = aes_encrypt_key(key, keySize, &ctx);
    }
    if (ret == EXIT_FAILURE) {
        return (*env)->NewByteArray(env, 0);
    }

    switch (mode) {
        case 0:
            // ECB
            ret = aes_ecb_decrypt(data, obuf, len, &deCtx);
            break;
        case 1:
            // CBC
            ret = aes_cbc_decrypt(data, obuf, len, iv, &deCtx);
            break;
        case 2:
            // CFB
            ret = aes_cfb_decrypt(data, obuf, len, iv, &ctx);
            break;
        case 3:
            // OFB
            ret = aes_ofb_decrypt(data, obuf, len, iv, &ctx);
            break;
        case 4:
            // CTR
            ret = aes_ctr_decrypt(data, obuf, len, iv, aes_ctr_cbuf_inc, &ctx);
            break;
        default:
            ret = EXIT_FAILURE;
            break;
    }

    if (ret == EXIT_FAILURE) {
        return (*env)->NewByteArray(env, 0);
    }

    jbyteArray returnBytes = as_byte_array(env, obuf, dataSize);

    release_jbyte_array(env, data_jbyteArray, data);
    release_jbyte_array(env, key_jbyteArray, key);
    release_jbyte_array(env, iv_jbyteArray, iv);
    return returnBytes;
}