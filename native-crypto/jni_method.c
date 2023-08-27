//
// Created by QuincySx on 2021/4/17.
//
#include <stdlib.h>
#include <string.h>
#include "include/jni_method.h"

// Utils for non-modifying operations (final)
unsigned char *final_array_to_c(JNIEnv *env, jbyteArray array, int *len) {
    *len = (*env)->GetArrayLength(env, array);
    unsigned char *buf = malloc(*len + 1);
    (*env)->GetByteArrayRegion(env, array, 0, *len, (jbyte *) buf);
    return buf;
}

void final_array_release(unsigned char *buf) {
    free(buf);
}

// Utils for modifiable operations (copy)
unsigned char *copy_array_to_c(JNIEnv *env, jbyteArray array, int *len) {
    *len = (*env)->GetArrayLength(env, array);
    return (unsigned char *) (*env)->GetByteArrayElements(env, array, NULL);
}

void copy_array_release(JNIEnv *env, jbyteArray array, unsigned char *buf) {
    (*env)->ReleaseByteArrayElements(env, array, (jbyte *) buf, 0);
}

// Utils for critical operations (critical)
unsigned char *critical_array_to_c(JNIEnv *env, jbyteArray array, int *len) {
    *len = (*env)->GetArrayLength(env, array);
    return (unsigned char *) (*env)->GetPrimitiveArrayCritical(env, array, NULL);
}

void critical_array_release(JNIEnv *env, jbyteArray array, unsigned char *buf) {
    (*env)->ReleasePrimitiveArrayCritical(env, array, (jbyte *) buf, 0);
}

// Conversion utility
jbyteArray as_byte_array(JNIEnv *env, unsigned char *buf, int len) {
    jbyteArray array = (*env)->NewByteArray(env, len);
    (*env)->SetByteArrayRegion(env, array, 0, len, (jbyte *) buf);
    return array;
}
