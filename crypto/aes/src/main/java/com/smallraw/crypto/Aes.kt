package com.smallraw.crypto

import androidx.annotation.IntDef
import com.smallraw.crypto.paddings.BlockCipherPadding
import com.smallraw.crypto.paddings.PKCS7Padding
import java.security.SecureRandom

/**
 * 秘钥长度 128 位以上。
 * AES 算法如果采用 CBC 模式：每次加密时 IV 必须采用密码学安全的伪随机发生器（如/dev/urandom）,禁止填充全 0 等固定值。
 * AES 算法如采用 GCM 模式，nonce 须采用密码学安全的伪随机数。
 * AES 算法避免使用 ECB 模式，推荐使用 GCM 模式。
 */
class Aes {
    companion object {
        init {
            System.loadLibrary("aes-wrapper")
        }

        private const val AES_BLOCK_SIZE = 16
    }

    @IntDef(Mode.CBC, Mode.CFB, Mode.OFB, Mode.CTR, Mode.ECB)
    annotation class Mode {
        companion object {
            const val ECB = 0
            const val CBC = 1
            const val CFB = 2
            const val OFB = 3
            const val CTR = 4
        }
    }

    private fun padding(
        padding: BlockCipherPadding,
        data: ByteArray,
        blockSize: Int = AES_BLOCK_SIZE
    ): ByteArray {
        val paddingLength = blockSize - data.size % blockSize
        if (paddingLength == blockSize) {
            return data
        }

        val newData = ByteArray(data.size + paddingLength)
        System.arraycopy(data, 0, newData, 0, data.size)

        padding.init(SecureRandom())
        padding.addPadding(newData, data.size)
        return newData
    }

    private fun unPadding(
        padding: BlockCipherPadding,
        data: ByteArray,
        blockSize: Int = AES_BLOCK_SIZE
    ): ByteArray {
        padding.init(SecureRandom())
        return try {
            val padCount = padding.padCount(data)
            data.copyOf(data.size - padCount)
        } catch (e: IllegalArgumentException) {
            data
        }
    }

    fun generateIV(): ByteArray {
        val iv = ByteArray(256)
        SecureRandom().nextBytes(iv)
        return iv
    }

    fun encryptECB(
        key: ByteArray,
        data: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        return encrypt(key, padding(padding, data), mode = Mode.ECB)
    }

    fun decryptECB(
        key: ByteArray,
        data: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        val decrypt = decrypt(key, data, mode = Mode.ECB)
        return unPadding(PKCS7Padding(), decrypt)
    }

    fun encryptCBC(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        return encrypt(key, padding(padding, data), iv, mode = Mode.CBC)
    }

    fun decryptCBC(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        val decrypt = decrypt(key, data, iv, mode = Mode.CBC)
        return unPadding(PKCS7Padding(), decrypt)
    }

    fun encryptCFB(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        return encrypt(key, padding(padding, data), iv, mode = Mode.CFB)
    }

    fun decryptCFB(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        val decrypt = decrypt(key, data, iv, mode = Mode.CFB)
        return unPadding(PKCS7Padding(), decrypt)
    }

    fun encryptOFB(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        return encrypt(key, padding(padding, data), iv, mode = Mode.OFB)
    }

    fun decryptOFB(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        val decrypt = decrypt(key, data, iv, mode = Mode.OFB)
        return unPadding(PKCS7Padding(), decrypt)
    }

    fun encryptCTR(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        return encrypt(key, padding(padding, data), iv, mode = Mode.CTR)
    }

    fun decryptCTR(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray,
        padding: BlockCipherPadding = PKCS7Padding()
    ): ByteArray {
        val decrypt = decrypt(key, data, iv, mode = Mode.CTR)
        return unPadding(PKCS7Padding(), decrypt)
    }

    external fun encrypt(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray = byteArrayOf(),
        mode: Int = Mode.CBC
    ): ByteArray

    external fun decrypt(
        key: ByteArray,
        data: ByteArray,
        iv: ByteArray = byteArrayOf(),
        mode: Int = Mode.CBC
    ): ByteArray
}