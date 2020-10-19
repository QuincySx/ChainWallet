package com.smallraw.chain.eos.crypto

import com.smallraw.chain.eos.Signature
import com.smallraw.crypto.core.crypto.Sha256
import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.lib.crypto.Secp256K1
import java.security.PrivateKey
import java.util.*

class Secp256k1Signer {
    @Throws(JNICallException::class)
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        var nonce: Byte = 0
        var i: Int
        var r: ByteArray
        var s: ByteArray

        while (true) {
            val signMessage = if (nonce > 0) {
                Sha256.sha256(message + byteArrayOf(nonce))
            } else {
                message
            }
            nonce++
            val sign = Secp256K1.ethSign(privateKey.encoded, signMessage)
            r = sign[0]?.copyOfRange(0, 32) ?: byteArrayOf()
            s = sign[0]?.copyOfRange(32, 64) ?: byteArrayOf()

            val der = toDER(r, s)
            val lenR: Byte = der.get(3)
            val lenS: Byte = der.get(5 + lenR)
            if (lenR.toInt() == 32 && lenS.toInt() == 32) {
                i = (sign.getOrNull(1)?.getOrNull(0))?.toInt() ?: 0
                i += 4 // compressed
                i += 27 // compact // 24 or 27 :( forcing odd-y 2nd key candidate)
                break
            }
        }

        val signBytes = ByteArray(65)
        signBytes[0] = i.toByte()
        System.arraycopy(r, 0, signBytes, 1, r.size)
        System.arraycopy(s, 0, signBytes, r.size + 1, s.size)

        return Signature(signBytes)
    }

    @Throws(JNICallException::class)
    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean {
        return Secp256K1.verify(publicKey, signature, message)
    }

    /**
     * toDER
     *
     * @param big
     * @return
     */
    private fun toDER(rBa: ByteArray, sBa: ByteArray): ByteArray {
        val sequence = ArrayList<Byte>(72)
        sequence.add(0x02.toByte())
        sequence.add(rBa.size.toByte())
        for (i in rBa.indices) {
            sequence.add(rBa[i])
        }
        sequence.add(0x02.toByte())
        sequence.add(sBa.size.toByte())
        for (i in sBa.indices) {
            sequence.add(sBa[i])
        }
        val len = sequence.size
        sequence.add(0, 0x30.toByte())
        sequence.add(1, len.toByte())
        val bf = ByteArray(sequence.size)
        for (i in bf.indices) {
            bf[i] = sequence[i]
        }
        return bf
    }
}