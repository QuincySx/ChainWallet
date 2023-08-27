package com.smallraw.crypto.core.jni

class CryptoJNI {
    companion object {
        init {
            System.loadLibrary("crypto-wrapper")
        }
    }

    external fun hexToStr(date: ByteArray): String?
    external fun strToHex(date: String): ByteArray?

    external fun base32Encode(date: ByteArray, padding: Boolean): String?
    external fun base32Decode(date: String, padding: Boolean): ByteArray?
    external fun base58Encode(date: ByteArray): String?
    external fun base58Decode(date: String): ByteArray?
    external fun base58EncodeCheck(date: ByteArray): String?
    external fun base58DecodeCheck(date: String): ByteArray?

    external fun sha256(date: ByteArray): ByteArray?
    external fun doubleSha256(date: ByteArray): ByteArray?
    external fun sha512(date: ByteArray): ByteArray?

    external fun ripemd160(date: ByteArray): ByteArray?

    external fun sha3_224(date: ByteArray): ByteArray?
    external fun sha3_384(date: ByteArray): ByteArray?
    external fun sha3_512(date: ByteArray): ByteArray?
    external fun sha3_256(date: ByteArray): ByteArray?
    external fun doubleSha3_256(date: ByteArray): ByteArray?

    external fun keccak_224(date: ByteArray): ByteArray?
    external fun keccak_256(date: ByteArray): ByteArray?
    external fun keccak_384(date: ByteArray): ByteArray?
    external fun keccak_512(date: ByteArray): ByteArray?
    external fun doubleKeccak_256(date: ByteArray): ByteArray?

    external fun hmac_sha256(
        key: ByteArray,
        message: ByteArray,
    ): ByteArray?

    external fun hmac_sha512(
        key: ByteArray,
        message: ByteArray,
    ): ByteArray?

    external fun sig_to_der(
        sign: ByteArray,
    ): ByteArray?

    external fun der_to_sig(
        der: ByteArray,
    ): ByteArray?

    external fun blake2b(input: ByteArray, outlen: Int): ByteArray?
    external fun blake2bWithKey(input: ByteArray, key: ByteArray, outlen: Int): ByteArray?

    external fun blake2s(input: ByteArray, outlen: Int): ByteArray?
    external fun blake2sWithKey(input: ByteArray, key: ByteArray, outlen: Int): ByteArray?

    external fun blake_256(input: ByteArray): ByteArray?

    external fun pbkdf2_hmac_sha256(
        inputs: ByteArray, salt: ByteArray, iterations: Int, byteCount: Int
    ): ByteArray?

    external fun pbkdf2_hmac_sha512(inputs: ByteArray, salt: ByteArray, iterations: Int, byteCount: Int): ByteArray?
}