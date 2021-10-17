package com.smallraw.chain.ethereum.supplement

import com.smallraw.chain.ethereum.PrivateKey
import com.smallraw.crypto.core.extensions.toByteArray

/**
 * Signed Data Standard
 * see http://eips.ethereum.org/EIPS/eip-191
 */
fun PrivateKey.signWithEIP191(version: Byte, versionSpecificData: ByteArray, message: ByteArray) =
    sign(0x19.toByte().toByteArray() + version.toByteArray() + versionSpecificData + message)

fun PrivateKey.signWithPersonalSign(message: ByteArray) =
    signWithEIP191(
        0x45 /* 0x45 is "E" */,
        ("thereum Signed Message:\n" + message.size).toByteArray(),
        message
    )