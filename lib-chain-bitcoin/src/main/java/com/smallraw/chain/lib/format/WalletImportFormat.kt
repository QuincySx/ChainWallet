package com.smallraw.chain.lib.format

import com.smallraw.chain.lib.crypto.Base58

/**
 * 电子钱包导入格式（WIF，也称为电子钱包导出格式）是一种对 ECDSA 私钥进行编码的方法，以便于复制。
 * see https://en.bitcoin.it/wiki/Wallet_import_format
 */
class WalletImportFormat(
    private val testNet: Boolean = false,
    private val compressed: Boolean = true
) {
    companion object {
        const val MAIN_NET_WIF_PRIVATE_KEY_PREFIX = 0x80.toByte()
        const val TEST_NET_WIF_PRIVATE_KEY_PREFIX = 0xef.toByte()
        private const val COMPRESSED_WIF_PRIVATE_KEY_SUFFIX = 0x01.toByte()

        private const val RAW_PRIVATE_KEY_COMPRESSED_LENGTH = 38
        private const val RAW_PRIVATE_KEY_NO_COMPRESSED_LENGTH = 37

        fun decode(wifPrivateKey: String): WifDecodeResult {
            val decodePrivateKey =
                Base58.decodeCheck(wifPrivateKey) ?: return WifDecodeResult(false)

            var isCompressed = false
            if (decodePrivateKey.size == RAW_PRIVATE_KEY_COMPRESSED_LENGTH) {
                isCompressed = true
            } else if (decodePrivateKey.size != RAW_PRIVATE_KEY_NO_COMPRESSED_LENGTH) {
                return WifDecodeResult(false)
            }

            var isTestNet = false
            if (decodePrivateKey[0].toInt() and 0xFF == TEST_NET_WIF_PRIVATE_KEY_PREFIX.toInt()) {
                isTestNet = true
            }

            val privateKey = ByteArray(32)
            System.arraycopy(decodePrivateKey, 1, privateKey, 0, privateKey.size)

            return WifDecodeResult(true, privateKey, isTestNet, isCompressed)
        }
    }

    fun format(privateKey: ByteArray): String? {
        val rawPrivateKey =
            ByteArray((if (isCompressed()) RAW_PRIVATE_KEY_COMPRESSED_LENGTH else RAW_PRIVATE_KEY_NO_COMPRESSED_LENGTH) - 4)
        System.arraycopy(privateKey, 0, rawPrivateKey, 1, privateKey.size)

        rawPrivateKey[0] =
            if (isTestNet()) TEST_NET_WIF_PRIVATE_KEY_PREFIX else MAIN_NET_WIF_PRIVATE_KEY_PREFIX
        if (isCompressed()) {
            rawPrivateKey[rawPrivateKey.size - 1] = COMPRESSED_WIF_PRIVATE_KEY_SUFFIX
        }

        return Base58.encodeCheck(rawPrivateKey)
    }

    private fun isTestNet(): Boolean = testNet

    private fun isCompressed(): Boolean = compressed

    data class WifDecodeResult(
        val success: Boolean,
        val privateKey: ByteArray = byteArrayOf(),
        val testNet: Boolean = true,
        val compressed: Boolean = true
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as WifDecodeResult

            if (success != other.success) return false
            if (!privateKey.contentEquals(other.privateKey)) return false
            if (testNet != other.testNet) return false
            if (compressed != other.compressed) return false

            return true
        }

        override fun hashCode(): Int {
            var result = success.hashCode()
            result = 31 * result + privateKey.contentHashCode()
            result = 31 * result + testNet.hashCode()
            result = 31 * result + compressed.hashCode()
            return result
        }
    }
}