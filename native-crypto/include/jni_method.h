//
// Created by QuincySx on 2021/4/17.
//

#ifndef CHAINWALLET_JNI_METHOD_H
#define CHAINWALLET_JNI_METHOD_H

#include <jni.h>

// Utils for non-modifying operations (final)
unsigned char *final_array_to_c(JNIEnv *env, jbyteArray array, int *len);

void final_array_release(unsigned char *buf);

// Utils for modifiable operations (copy)
unsigned char *copy_array_to_c(JNIEnv *env, jbyteArray array, int *len);

void copy_array_release(JNIEnv *env, jbyteArray array, unsigned char *buf);

// Utils for critical operations (critical)
unsigned char *critical_array_to_c(JNIEnv *env, jbyteArray array, int *len);

void critical_array_release(JNIEnv *env, jbyteArray array, unsigned char *buf);

// Conversion utility
jbyteArray as_byte_array(JNIEnv *env, unsigned char *buf, int len);


#endif //CHAINWALLET_JNI_METHOD_H
