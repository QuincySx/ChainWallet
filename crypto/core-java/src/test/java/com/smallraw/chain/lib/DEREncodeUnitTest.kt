package com.smallraw.chain.lib

import com.smallraw.chain.lib.extensions.hexToBytes
import com.smallraw.chain.lib.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.spongycastle.asn1.ASN1Integer
import org.spongycastle.asn1.DERSequenceGenerator
import java.io.ByteArrayOutputStream
import java.math.BigInteger


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DEREncodeUnitTest {
    val LARGEST_PRIVATE_KEY = BigInteger(
        "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141",
        16
    ) //SECP256K1_N

    @Test
    fun test_der_encode() {
        val hexToBytes =
            "753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f42bb43fc9c6a3877e6aa1312059f1c4a9d376cf21c750c30b09fc2be7096be3e3".hexToBytes()

        val r: BigInteger = BigInteger( hexToBytes.copyOfRange(0, 32))
        var s: BigInteger = BigInteger( hexToBytes.copyOfRange(32, 64))

        val largestAllowedS =
            BigInteger("7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5D576E7357A4501DDFE92F46681B20A0", 16)
        //SECP256K1_N_DIV_2
        //SECP256K1_N_DIV_2
        if (s.compareTo(largestAllowedS) > 0) {
            //https://github.com/bitcoin/bips/blob/master/bip-0062.mediawiki#low-s-values-in-signatures
            s = LARGEST_PRIVATE_KEY.subtract(s)
        }

        val baos = ByteArrayOutputStream(72)
        val derGen = DERSequenceGenerator(baos)
        derGen.addObject(ASN1Integer(r))
        derGen.addObject(ASN1Integer(s))
        derGen.close()
        val toByteArray = baos.toByteArray()

        println(toByteArray.toHex())

        Assert.assertEquals(
            toByteArray.toHex(),
            "30440220753FA0FED5C3367E1E868CECA970C3D20825C4237790EFDD25C78C917F7090F402202BB43FC9C6A3877E6AA1312059F1C4A9D376CF21C750C30B09FC2BE7096BE3E3"
        )
    }
}