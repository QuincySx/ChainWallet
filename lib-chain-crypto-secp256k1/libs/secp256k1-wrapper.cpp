//
// Created by zaryab on 4/13/20.
//

#include <jni.h>

#include "secp256k1-cxx.hpp"
#include <string>
#include <cstring>

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
JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_createPublicKey(JNIEnv *env,
                                                             jobject byteObj /* this */,
                                                             jbyteArray privKeyBytes,
                                                             jboolean compressed_jbool) {
    const auto *bytes = (const unsigned char *) env->GetByteArrayElements(privKeyBytes,
                                                                          nullptr);
    std::vector<uint8_t> priv(32);
    priv.assign(bytes, bytes + 32);

    auto compressed = compressed_jbool == JNI_TRUE;

    auto pubKey = createPublicKeyFromPriv(priv, compressed);
    unsigned char *pbKey = pubKey.data();
    auto size = pubKey.size();
//    assert(size == 65);
    jbyteArray ret = env->NewByteArray(size);
    env->SetByteArrayRegion(ret, 0, size, (jbyte *) pbKey);

    return ret;
}

/**
 * @brief returns public key in compressed/uncompressed form
 * @param env
 * @param byteObj
 * @param compressed whether public key should be compressed or uncompressed
 * @return byte[] public key
 */
JNIEXPORT jbyteArray JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_sign(JNIEnv *env,
                                                  jobject byteObj /* this */,
                                                  jbyteArray private_key_jbytearray,
                                                  jbyteArray message_jbytearray) {
    const auto *private_key = (const unsigned char *) env->GetByteArrayElements(
            private_key_jbytearray,
            nullptr);
    std::vector<uint8_t> priv(32);
    priv.assign(private_key, private_key + 32);

    const auto *messages = (const unsigned char *) env->GetByteArrayElements(message_jbytearray,
                                                                             nullptr);
    std::vector<uint8_t> msg(32);
    msg.assign(messages, messages + 32);

    std::tuple<std::vector<uint8_t>, bool> signatureTuple = Sign(priv, msg.data());
    std::vector<uint8_t> signatureVec = std::get<0>(signatureTuple);
    bool signedr = std::get<1>(signatureTuple);

    if (signedr) {
        unsigned char *signature = signatureVec.data();
        auto size = signatureVec.size();
//        assert(size == 65);
        jbyteArray ret = env->NewByteArray(size);
        env->SetByteArrayRegion(ret, 0, size, (jbyte *) signature);

        return ret;
    } else {
        return env->NewByteArray(0);
    }

}

JNIEXPORT jboolean JNICALL
Java_com_smallraw_chain_lib_jni_Secp256k1JNI_verify(JNIEnv *env,
                                                    jobject byteObj /* this */,
                                                    jbyteArray public_key_jbytearray,
                                                    jbyteArray signature_jbytearray,
                                                    jbyteArray message_jbytearray) {
    const auto *publicKey = (const unsigned char *) env->GetByteArrayElements(public_key_jbytearray,
                                                                              nullptr);
    std::vector<uint8_t> pub(32);
    pub.assign(publicKey, publicKey + 32);

    const auto *signature = (const unsigned char *) env->GetByteArrayElements(signature_jbytearray,
                                                                              nullptr);
    std::vector<uint8_t> sign(32);
    sign.assign(signature, signature + 32);

    const auto *message = (const unsigned char *) env->GetByteArrayElements(message_jbytearray,
                                                                            nullptr);
    std::vector<uint8_t> msg(32);
    msg.assign(message, message + 32);

    bool verify = true;// Verify(msg.data(), sign, pub);

    return verify ? JNI_TRUE : JNI_FALSE;;
}

#ifdef __cplusplus
}
#endif
