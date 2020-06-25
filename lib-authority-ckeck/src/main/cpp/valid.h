#ifndef VALID_H_
#define VALID_H_

#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include <android/log.h>

#define TAG    "AuthorityKey Jni Log"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

char *getSignatureSha1(JNIEnv *env, jobject context_object);

jboolean checkValidity(char *sha1);

#endif /* VALID_H_ */