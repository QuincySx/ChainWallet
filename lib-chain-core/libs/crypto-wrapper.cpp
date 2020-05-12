//
// Created by zaryab on 4/13/20.
//

#include <jni.h>

#include "base58.h"
#include <string>
#include <cstring>
#include <vector>

#ifdef __cplusplus
extern "C" {
#endif


/**
 * @brief creates public key from given bytes(private key) and returns it in uncompressed form
 * @param env
 * @param byteObj
 * @param privKeyBytes
 * @return public key byte[]
 */
JNIEXPORT jstring JNICALL
Java_com_smallraw_chain_lib_jni_CryptoJNI_base58EncodeCheck(JNIEnv *env,
                                                            jobject byteObj /* this */,
                                                            jbyteArray bytes_jbyteArray) {
    const auto *bytes = (const unsigned char *) env->GetByteArrayElements(bytes_jbyteArray,
                                                                          nullptr);
    std::vector<uint8_t> data(32);
    data.assign(bytes, bytes + 32);

    char out[32];
    if (base58_encode_check(data.data(), data.size(), out, data.size()) != 1) {
        return env->NewStringUTF({});
    }
    return env->NewStringUTF(out);
}

#ifdef __cplusplus
}
#endif
