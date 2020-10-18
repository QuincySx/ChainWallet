package com.smallraw.chain.ethereum.extensions

import com.smallraw.chain.ethereum.rpl.RLPElement
import java.math.BigInteger

fun RLPElement?.toInt(): Int {
    val rlpData = this?.rlpData
    return if (this == null || rlpData == null || rlpData.isEmpty()) 0 else BigInteger(
        1,
        rlpData
    ).toInt()
}

fun RLPElement?.toLong(): Long {
    val rlpData = this?.rlpData
    return if (this == null || rlpData == null || rlpData.isEmpty()) 0 else BigInteger(
        1,
        rlpData
    ).toLong()
}

fun RLPElement?.toBigInteger(): BigInteger {
    val rlpData = this?.rlpData
    return if (this == null || rlpData == null || rlpData.isEmpty()) BigInteger.ZERO else BigInteger(
        1,
        rlpData
    )
}

fun RLPElement?.asString(): String {
    val rlpData = this?.rlpData
    return if (this == null || rlpData == null || rlpData.isEmpty()) "" else String(rlpData)
}
