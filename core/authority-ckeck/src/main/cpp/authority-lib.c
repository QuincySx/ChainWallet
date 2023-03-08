#include <jni.h>
#include "valid.h"

#if __has_include ("keys.secret")
#   define HAS_KEYS 1
#   include "keys.secret"
#else
#   define HAS_KEYS 0

#   include "keys.c"

#endif

JNIEXPORT jboolean JNICALL
Java_com_smallraw_authority_AuthorityKey_checkValidity(
        JNIEnv *env,
        jobject byteObj,
        jobject contextObject) {

    if (!checkSecurityPermission(env, contextObject, (char **) auth_app_sha1, auth_app_sha1_size)) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

JNIEXPORT jstring JNICALL
Java_com_smallraw_authority_AuthorityKey_getAuthorityKey(
        JNIEnv *env,
        jobject byteObj,
        jobject contextObject) {

    jboolean result = !checkSecurityPermission(env, contextObject, (char **) auth_app_sha1,
                                               auth_app_sha1_size);

    // 不会被 Ida64 Pro 破解出来
    int n = 0;
    char AUTH_KEY[32 + 1];

    AUTH_KEY[n++] = '5';
    AUTH_KEY[n++] = '2';
    AUTH_KEY[n++] = 'b';
    AUTH_KEY[n++] = '7';
    AUTH_KEY[n++] = '3';
    AUTH_KEY[n++] = 'a';
    AUTH_KEY[n++] = 'b';
    AUTH_KEY[n++] = '4';
    AUTH_KEY[n++] = '2';
    AUTH_KEY[n++] = '4';
    AUTH_KEY[n++] = '5';
    AUTH_KEY[n++] = '4';
    AUTH_KEY[n++] = '3';
    AUTH_KEY[n++] = 'e';
    AUTH_KEY[n++] = 'f';
    AUTH_KEY[n++] = 'd';
    AUTH_KEY[n++] = '3';
    AUTH_KEY[n++] = '9';
    AUTH_KEY[n++] = '2';
    AUTH_KEY[n++] = '7';
    AUTH_KEY[n++] = '6';
    AUTH_KEY[n++] = 'b';
    AUTH_KEY[n++] = 'a';
    AUTH_KEY[n++] = 'd';
    AUTH_KEY[n++] = '6';
    AUTH_KEY[n++] = 'e';
    AUTH_KEY[n++] = '7';
    AUTH_KEY[n++] = '2';
    AUTH_KEY[n++] = '1';
    AUTH_KEY[n++] = '6';
    AUTH_KEY[n++] = '9';
    AUTH_KEY[n++] = '3';
    AUTH_KEY[n] = '\0';//utf8 字符串结尾

    if (result) {
        return (*env)->NewStringUTF(env, AUTH_KEY);
    } else {
        return (*env)->NewStringUTF(env, NULL);
    }
}