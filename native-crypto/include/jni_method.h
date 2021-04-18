//
// Created by QuincySx on 2021/4/17.
//

#ifndef CHAINWALLET_JNI_METHOD_H
#define CHAINWALLET_JNI_METHOD_H
#include <jni.h>

unsigned char *as_unsigned_char_array(JNIEnv *env, jbyteArray array, int *len);

jbyteArray as_byte_array(JNIEnv *env, unsigned char *buf, int len);

void release_jbyte_array(JNIEnv *env, jbyteArray array, void *carray);

#endif //CHAINWALLET_JNI_METHOD_H
