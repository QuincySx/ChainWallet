#include <jni.h>
#include"valid.c"

JNIEXPORT jboolean JNICALL
Java_com_smallraw_authority_AuthorityKey_00024Companion_checkValidity(
        JNIEnv *env,
        jobject byteObj,
        jobject contextObject) {

    char *sha1 = getSha1(env, contextObject);

    jboolean result = checkValidity(env, sha1);

    return result;
}

JNIEXPORT jstring JNICALL
Java_com_smallraw_authority_AuthorityKey_00024Companion_getAuthorityKey(
        JNIEnv *env,
        jobject byteObj,
        jobject contextObject) {
    char *sha1 = getSha1(env, contextObject);
    jboolean result = checkValidity(env, sha1);

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