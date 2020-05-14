package com.smallraw.chain.lib.jni

object CryptoJNI {
    init {
        System.loadLibrary("crypto-wrapper")
    }

    external fun hexToStr(date: ByteArray, dataSize: Int = date.size): String?
    external fun strToHex(date: String): ByteArray?

    external fun base58Encode(date: ByteArray, dataSize: Int = date.size): String?
    external fun base58Decode(date: String): ByteArray?
    external fun base58EncodeCheck(date: ByteArray, dataSize: Int = date.size): String?
    external fun base58DecodeCheck(date: String): ByteArray?

    external fun sha256(date: ByteArray, dataSize: Int = date.size): ByteArray?
    external fun doubleSha256(date: ByteArray, dataSize: Int = date.size): ByteArray?

    external fun ripemd160(date: ByteArray, dataSize: Int = date.size): ByteArray?

    external fun sha3_256(date: ByteArray, dataSize: Int = date.size): ByteArray?
    external fun doubleSha3_256(date: ByteArray, dataSize: Int = date.size): ByteArray?

    external fun keccak_256(date: ByteArray, dataSize: Int = date.size): ByteArray?
    external fun doubleKeccak_256(date: ByteArray, dataSize: Int = date.size): ByteArray?
}