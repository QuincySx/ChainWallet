//
// Created by q7728 on 2021/4/3.
//
#include <jni.h>
#include "aes/aes.h"
#include "include/jni_method.h"


JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_Aes_encrypt(JNIEnv *env, jobject thiz,
                                     jbyteArray j_key,
                                     jbyteArray j_data,
                                     jbyteArray j_iv,
                                     jint j_mode) {

    int key_size, data_size, iv_size;
    unsigned char *key = copy_array_to_c(env, j_key, &key_size);
    unsigned char *data = copy_array_to_c(env, j_data, &data_size);
    unsigned char *iv = copy_array_to_c(env, j_iv, &iv_size);

    int len = data_size;
    int mode = j_mode;

    unsigned char obuf[data_size];

    aes_init();
    aes_encrypt_ctx ctx = {0};
    int ret = aes_encrypt_key(key, key_size, &ctx);
    if (ret == EXIT_FAILURE) {
        copy_array_release(env, j_data, data);
        copy_array_release(env, j_key, key);
        copy_array_release(env, j_iv, iv);
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
        copy_array_release(env, j_data, data);
        copy_array_release(env, j_key, key);
        copy_array_release(env, j_iv, iv);
        return (*env)->NewByteArray(env, 0);
    }

    jbyteArray returnBytes = as_byte_array(env, obuf, data_size);

    copy_array_release(env, j_data, data);
    copy_array_release(env, j_key, key);
    copy_array_release(env, j_iv, iv);
    return returnBytes;
}

JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_crypto_Aes_decrypt(JNIEnv *env, jobject thiz,
                                     jbyteArray key_jbyteArray,
                                     jbyteArray data_jbyteArray,
                                     jbyteArray iv_jbyteArray,
                                     jint mode_jint) {
    int key_size, data_size, iv_size;
    unsigned char *key = copy_array_to_c(env, key_jbyteArray, &key_size);
    unsigned char *data = copy_array_to_c(env, data_jbyteArray, &data_size);
    unsigned char *iv = copy_array_to_c(env, iv_jbyteArray, &iv_size);

    int len = data_size;
    int mode = mode_jint;

    unsigned char obuf[data_size];

    aes_init();
    aes_encrypt_ctx ctx = {0};
    aes_decrypt_ctx deCtx = {0};
    int ret;
    if (mode == 0 || mode == 1) {
        ret = aes_decrypt_key(key, key_size, &deCtx);
    } else {
        ret = aes_encrypt_key(key, key_size, &ctx);
    }
    if (ret == EXIT_FAILURE) {
        copy_array_release(env, data_jbyteArray, data);
        copy_array_release(env, key_jbyteArray, key);
        copy_array_release(env, iv_jbyteArray, iv);
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
        copy_array_release(env, data_jbyteArray, data);
        copy_array_release(env, key_jbyteArray, key);
        copy_array_release(env, iv_jbyteArray, iv);
        return (*env)->NewByteArray(env, 0);
    }

    jbyteArray returnBytes = as_byte_array(env, obuf, data_size);

    copy_array_release(env, data_jbyteArray, data);
    copy_array_release(env, key_jbyteArray, key);
    copy_array_release(env, iv_jbyteArray, iv);
    return returnBytes;
}