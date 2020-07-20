package com.smallraw.chain.lib.bitcoin.convert

import com.smallraw.chain.lib.bitcoin.network.Network
import com.smallraw.chain.lib.crypto.Base58

/**
 * 电子钱包导入格式（WIF，也称为电子钱包导出格式）是一种对 ECDSA 私钥进行编码的方法，以便于复制。
 * see https://en.bitcoin.it/wiki/Wallet_import_format
 */
class WalletImportFormat(
    private val network: Network,
    private val compressed: Boolean = true
) {
    companion object {
        private const val COMPRESSED_WIF_PRIVATE_KEY_SUFFIX = 0x01.toByte()

        private const val RAW_PRIVATE_KEY_COMPRESSED_LENGTH = 38
        private const val RAW_PRIVATE_KEY_NO_COMPRESSED_LENGTH = 37

        /**
         * 将 WIF 格式的私钥转换为普通私钥
         */
        fun decode(wifPrivateKey: String): WifDecodeResult {
            val decodePrivateKey =
                Base58.decodeCheck(wifPrivateKey)

            var isCompressed = when (decodePrivateKey.size + 4) {
                RAW_PRIVATE_KEY_COMPRESSED_LENGTH -> {
                    true
                }
                RAW_PRIVATE_KEY_NO_COMPRESSED_LENGTH -> {
                    false
                }
                else -> {
                    return WifDecodeResult(false)
                }
            }

            val privateKey = ByteArray(32)
            System.arraycopy(decodePrivateKey, 1, privateKey, 0, privateKey.size)

            return WifDecodeResult(true, privateKey, decodePrivateKey[0].toInt(), isCompressed)
        }
    }

    /**
     * 将普通私钥转换为 WIF 格式的私钥
     */
    fun format(privateKey: ByteArray): String {
        val rawPrivateKey =
            ByteArray((if (isCompressed()) RAW_PRIVATE_KEY_COMPRESSED_LENGTH else RAW_PRIVATE_KEY_NO_COMPRESSED_LENGTH) - 4)
        System.arraycopy(privateKey, 0, rawPrivateKey, 1, privateKey.size)

        rawPrivateKey[0] = (network.addressWifVersion and 0xFF).toByte()
        if (isCompressed()) {
            rawPrivateKey[rawPrivateKey.size - 1] = COMPRESSED_WIF_PRIVATE_KEY_SUFFIX
        }

        return Base58.encodeCheck(rawPrivateKey)
    }

    private fun isCompressed(): Boolean = compressed

    data class WifDecodeResult(
        val success: Boolean,
        val privateKey: ByteArray = byteArrayOf(),
        val addressVersion: Int = -1,
        val compressed: Boolean = true
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as WifDecodeResult

            if (success != other.success) return false
            if (!privateKey.contentEquals(other.privateKey)) return false
            if (addressVersion != other.addressVersion) return false
            if (compressed != other.compressed) return false

            return true
        }

        override fun hashCode(): Int {
            var result = success.hashCode()
            result = 31 * result + privateKey.contentHashCode()
            result = 31 * result + addressVersion.hashCode()
            result = 31 * result + compressed.hashCode()
            return result
        }
    }
}