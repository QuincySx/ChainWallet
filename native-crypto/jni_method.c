//
// Created by QuincySx on 2021/4/17.
//
#include <stdlib.h>
#include "include/jni_method.h"

unsigned char *as_unsigned_char_array(JNIEnv *env, jbyteArray array, int *len) {
    *len = (*env)->GetArrayLength(env, array);
    // unsigned char *buf = (unsigned char *) calloc(*len + 1, sizeof(char));
    unsigned char *buf = malloc(*len + 1);
    (*env)->GetByteArrayRegion(env, array, 0, *len, (jbyte *) buf);
    return buf;
}

jbyteArray as_byte_array(JNIEnv *env, unsigned char *buf, int len) {
    jbyteArray array = (*env)->NewByteArray(env, len);
    (*env)->SetByteArrayRegion(env, array, 0, len, (jbyte *) buf);

    //int i;
    //for (i = 0;i < len;++i) printf("%02x",(unsigned int) buf[i]); printf(" ");
    //printf("\n");

    return array;
}

void release_jbyte_array(JNIEnv *env, jbyteArray array, void *carray) {
    ((*env)->ReleasePrimitiveArrayCritical(env, array, (jbyte *) carray, 0));
}