#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include <android/log.h>

#define TAG    "AuthorityKey Jni Log"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

// 查看签名信息：gradlew sR
// 签名信息
const char *app_sha1[] = {"9E08CE4C3F5D243197AFA53DD9E2255C825CAD3E",
                          "81BA0CF9134C6415F34C3BCC854913A53C71415E"};

const char hexcode[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                        'F'};

char *getSha1(JNIEnv *env, jobject context_object) {
    //上下文对象
    jclass context_class = (*env)->GetObjectClass(env, context_object);

    //反射获取PackageManager
    jmethodID methodId = (*env)->GetMethodID(env, context_class, "getPackageManager",
                                             "()Landroid/content/pm/PackageManager;");
    jobject package_manager = (*env)->CallObjectMethod(env, context_object, methodId);
    if (package_manager == NULL) {
        LOGD("package_manager is NULL!!!");
        return NULL;
    }

    //反射获取包名
    methodId = (*env)->GetMethodID(env, context_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name = (jstring) (*env)->CallObjectMethod(env, context_object, methodId);
    if (package_name == NULL) {
        LOGD("package_name is NULL!!!");
        return NULL;
    }
    (*env)->DeleteLocalRef(env, context_class);

    //获取PackageInfo对象
    jclass pack_manager_class = (*env)->GetObjectClass(env, package_manager);
    methodId = (*env)->GetMethodID(env, pack_manager_class, "getPackageInfo",
                                   "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    (*env)->DeleteLocalRef(env, pack_manager_class);
    jobject package_info = (*env)->CallObjectMethod(env, package_manager, methodId, package_name,
                                                    0x40);
    if (package_info == NULL) {
        LOGD("getPackageInfo() is NULL!!!");
        return NULL;
    }
    (*env)->DeleteLocalRef(env, package_manager);

    //获取签名信息
    jclass package_info_class = (*env)->GetObjectClass(env, package_info);
    jfieldID fieldId = (*env)->GetFieldID(env, package_info_class, "signatures",
                                          "[Landroid/content/pm/Signature;");
    (*env)->DeleteLocalRef(env, package_info_class);
    jobjectArray signature_object_array = (jobjectArray) (*env)->GetObjectField(env, package_info,
                                                                                fieldId);
    if (signature_object_array == NULL) {
        LOGD("signature is NULL!!!");
        return NULL;
    }
    jobject signature_object = (*env)->GetObjectArrayElement(env, signature_object_array, 0);
    (*env)->DeleteLocalRef(env, package_info);

    //签名信息转换成sha1值
    jclass signature_class = (*env)->GetObjectClass(env, signature_object);
    methodId = (*env)->GetMethodID(env, signature_class, "toByteArray", "()[B");
    (*env)->DeleteLocalRef(env, signature_class);
    jbyteArray signature_byte = (jbyteArray) (*env)->CallObjectMethod(env, signature_object,
                                                                      methodId);
    jclass byte_array_input_class = (*env)->FindClass(env, "java/io/ByteArrayInputStream");
    methodId = (*env)->GetMethodID(env, byte_array_input_class, "<init>", "([B)V");
    jobject byte_array_input = (*env)->NewObject(env, byte_array_input_class, methodId,
                                                 signature_byte);
    jclass certificate_factory_class = (*env)->FindClass(env,
                                                         "java/security/cert/CertificateFactory");
    methodId = (*env)->GetStaticMethodID(env, certificate_factory_class, "getInstance",
                                         "(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;");
    jstring x_509_jstring = (*env)->NewStringUTF(env, "X.509");
    jobject cert_factory = (*env)->CallStaticObjectMethod(env, certificate_factory_class, methodId,
                                                          x_509_jstring);
    methodId = (*env)->GetMethodID(env, certificate_factory_class, "generateCertificate",
                                   ("(Ljava/io/InputStream;)Ljava/security/cert/Certificate;"));
    jobject x509_cert = (*env)->CallObjectMethod(env, cert_factory, methodId, byte_array_input);
    (*env)->DeleteLocalRef(env, certificate_factory_class);
    jclass x509_cert_class = (*env)->GetObjectClass(env, x509_cert);
    methodId = (*env)->GetMethodID(env, x509_cert_class, "getEncoded", "()[B");
    jbyteArray cert_byte = (jbyteArray) (*env)->CallObjectMethod(env, x509_cert, methodId);
    (*env)->DeleteLocalRef(env, x509_cert_class);
    jclass message_digest_class = (*env)->FindClass(env, "java/security/MessageDigest");
    methodId = (*env)->GetStaticMethodID(env, message_digest_class, "getInstance",
                                         "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jstring sha1_jstring = (*env)->NewStringUTF(env, "SHA1");
    jobject sha1_digest = (*env)->CallStaticObjectMethod(env, message_digest_class, methodId,
                                                         sha1_jstring);
    methodId = (*env)->GetMethodID(env, message_digest_class, "digest", "([B)[B");
    jbyteArray sha1_byte = (jbyteArray) (*env)->CallObjectMethod(env, sha1_digest, methodId,
                                                                 cert_byte);
    (*env)->DeleteLocalRef(env, message_digest_class);

    //转换成char
    jsize array_size = (*env)->GetArrayLength(env, sha1_byte);
    unsigned char *sha1 = (unsigned char *) (*env)->GetByteArrayElements(env, sha1_byte, 0);
    char *hex_sha = malloc(array_size * 2 + 1);
    for (int i = 0; i < array_size; ++i) {
        hex_sha[2 * i] = hexcode[(sha1[i]) / 16];
        hex_sha[2 * i + 1] = hexcode[(sha1[i]) % 16];
    }
    hex_sha[array_size * 2] = '\0';

    return hex_sha;
}

jboolean checkValidity(JNIEnv *env, char *sha1) {
    //比较签名
    int size = sizeof(app_sha1) / sizeof(app_sha1[0]);
    for (int i = 0; i < size; ++i) {
        const char *current = app_sha1[i];
        if (strcmp(sha1, current) == 0) {
            LOGD("signature is success.");
            return JNI_TRUE;
        }
    }
    LOGD("signature is error.");
    return JNI_FALSE;
}