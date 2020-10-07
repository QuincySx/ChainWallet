package com.smallraw.chain.bitcoincore.crypto


import com.smallraw.chain.bitcoincore.execptions.BitcoinFormatException.AddressFormatException
import java.io.ByteArrayOutputStream
import java.util.*

class Bech32Segwit : Bech32() {
    companion object {
        /** Find the polynomial with value coefficients mod the generator as 30-bit.  */
        private fun polymod(values: ByteArray): Int {
            var c = 1
            for (v_i in values) {
                val c0 = c ushr 25 and 0xff
                c = c and 0x1ffffff shl 5 xor (v_i.toInt().and(0xFF))
                if (c0 and 1 != 0) c = c xor 0x3b6a57b2
                if (c0 and 2 != 0) c = c xor 0x26508e6d
                if (c0 and 4 != 0) c = c xor 0x1ea119fa
                if (c0 and 8 != 0) c = c xor 0x3d4233dd
                if (c0 and 16 != 0) c = c xor 0x2a1462b3
            }
            return c
        }

        /** Expand a address prefix for use in checksum computation.  */
        private fun expandPrefix(prefix: String): ByteArray {
            val prefixLength = prefix.length
            val ret = ByteArray(prefixLength * 2 + 1)
            for (i in 0 until prefixLength) {
                val c = prefix[i].toInt() and 0x7f // Limit to standard 7-bit ASCII
                ret[i] = (c ushr 5 and 0x07).toByte()
                ret[i + prefixLength + 1] = (c and 0x1f).toByte()
            }
            ret[prefixLength] = 0
            return ret
        }

        /** Verify a checksum.  */
        private fun verifyChecksum(prefix: String, values: ByteArray): Boolean {
            val prefixExpanded = expandPrefix(prefix)
            val combined = ByteArray(prefixExpanded.size + values.size)
            System.arraycopy(prefixExpanded, 0, combined, 0, prefixExpanded.size)
            System.arraycopy(values, 0, combined, prefixExpanded.size, values.size)
            return polymod(combined) == 1
        }

        /** Create a checksum.  */
        private fun createChecksum(prefix: String, values: ByteArray): ByteArray {
            val prefixExpanded = expandPrefix(prefix)
            val enc = ByteArray(prefixExpanded.size + values.size + 6)
            System.arraycopy(prefixExpanded, 0, enc, 0, prefixExpanded.size)
            System.arraycopy(values, 0, enc, prefixExpanded.size, values.size)
            val mod = polymod(enc) xor 1
            val ret = ByteArray(6)
            for (i in 0..5) {
                ret[i] = (mod ushr 5 * (5 - i) and 31).toByte()
            }
            return ret
        }

        /** Encode a Bech32 string.  */
        @Throws(AddressFormatException::class)
        fun encode(bech32: Bech32.Companion.Bech32Data): String? {
            return encode(bech32.hrp, bech32.data)
        }

        /** Encode a Bech32 string.  */
        @Throws(AddressFormatException::class)
        fun encode(prefix: String, values: ByteArray): String {
            var prefix = prefix
            if (prefix.length < 1) throw AddressFormatException("Human-readable part is too short")
            if (prefix.length > 83) throw AddressFormatException("Human-readable part is too long")
            prefix = prefix.toLowerCase(Locale.ROOT)
            val checksum = createChecksum(prefix, values)
            val combined = ByteArray(values.size + checksum.size)
            System.arraycopy(values, 0, combined, 0, values.size)
            System.arraycopy(checksum, 0, combined, values.size, checksum.size)
            val sb = StringBuilder(prefix.length + 1 + combined.size)
            sb.append(prefix)
            sb.append('1')
            for (b in combined) {
                sb.append(CHARSET[b.toInt()])
            }
            return sb.toString()
        }

        /** Decode a Bech32 string.  */
        @Throws(AddressFormatException::class)
        fun decode(str: String): Bech32.Companion.Bech32Data {
            var lower = false
            var upper = false
            if (str.length < 8) throw AddressFormatException("Input too short")
            if (str.length > 90) throw AddressFormatException("Input too long")
            for (i in 0 until str.length) {
                val c = str[i]
                if (c.toInt() < 33 || c.toInt() > 126) throw AddressFormatException("Characters out of range")
                if (c >= 'a' && c <= 'z') lower = true
                if (c >= 'A' && c <= 'Z') upper = true
            }
            if (lower && upper) throw AddressFormatException("Cannot mix upper and lower cases")
            val pos = str.lastIndexOf('1')
            if (pos < 1) throw AddressFormatException("Missing human-readable part")
            if (pos + 7 > str.length) throw AddressFormatException("Data part too short")
            val values = ByteArray(str.length - 1 - pos)
            for (i in 0 until str.length - 1 - pos) {
                val c = str[i + pos + 1]
                if (CHARSET_REV.get(c.toInt())
                        .toInt() == -1
                ) throw AddressFormatException("Characters out of range")
                values[i] = CHARSET_REV.get(c.toInt())
            }
            val prefix = str.substring(0, pos).toLowerCase(Locale.ROOT)
            if (!verifyChecksum(prefix, values)) throw AddressFormatException("Invalid checksum")
            return Bech32.Companion.Bech32Data(
                prefix,
                Arrays.copyOfRange(values, 0, values.size - 6)
            )
        }

        /** General power-of-2 base conversion  */
        @Throws(AddressFormatException::class)
        fun convertBits(
            data: ByteArray,
            start: Int,
            size: Int,
            fromBits: Int,
            toBits: Int,
            pad: Boolean
        ): ByteArray {
            var acc = 0
            var bits = 0
            val out = ByteArrayOutputStream(64)
            val maxv = (1 shl toBits) - 1
            val max_acc = (1 shl fromBits + toBits - 1) - 1
            for (i in 0 until size) {
                val value: Int = data[i + start].toInt() and 0xff
                if (value ushr fromBits != 0) {
                    throw AddressFormatException("Invalid data range: data[$i]=$value (fromBits=$fromBits)")
                }
                acc = acc shl fromBits or value and max_acc
                bits += fromBits
                while (bits >= toBits) {
                    bits -= toBits
                    out.write(acc ushr bits and maxv)
                }
            }
            if (pad) {
                if (bits > 0) out.write(acc shl toBits - bits and maxv)
            } else if (bits >= fromBits || acc shl toBits - bits and maxv != 0) {
                throw AddressFormatException("Could not convert bits, invalid padding")
            }
            return out.toByteArray()
        }
    }
}