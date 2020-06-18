package com.smallraw.chain.lib.bitcoin.transaction.serializers

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger

object DERSerializer {
    fun encodeToDER(r: BigInteger, s: BigInteger): ByteArray {
        var encodedBytes: ByteArray? = null
//        try {
//            ByteArrayOutputStream(80).use { outStream ->
//                val seq = DERSequenceGenerator(outStream)
//                seq.addObject(ASN1Integer(r))
//                seq.addObject(ASN1Integer(s))
//                seq.close()
//                encodedBytes = outStream.toByteArray()
//            }
//        } catch (exc: IOException) {
//            throw IllegalStateException("Unexpected IOException", exc)
//        }
        return encodedBytes!!
    }
}