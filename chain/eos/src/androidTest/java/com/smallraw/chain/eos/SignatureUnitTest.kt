package com.smallraw.chain.eos

import com.smallraw.crypto.core.crypto.Sha256
import com.smallraw.crypto.core.extensions.hexToByteArray
import org.junit.Assert
import org.junit.Test


class SignatureUnitTest {
    @Test
    fun test_signature() {
        val privateKey =
            PrivateKey.ofHex("0xc41e9c899be9e822e918b0206709c24f56ae174a6244676eb71c024cd4a0ad2a")
        Assert.assertArrayEquals(
            privateKey.getKey(),
            "4cb8333f0e11bf42cf6ecb92765d9b004b640d54f41ff00d5d3cabfcc8ae4584".hexToByteArray()
        )
        Assert.assertEquals(
            privateKey.toString(),
            "5JQ5H8ktrPeHyGU4gmEDUTK7jM8Te2QXc4J1NzeQk6ZinASQvCT"
        )

        val publicKey = privateKey.getPublicKey()
        Assert.assertEquals(
            publicKey.toString(),
            "EOS8EzuaoP3kELe2WorLyMwKRD3KNypJzGomXTjg6dB1tGEGitgKt"
        )

        val message = byteArrayOf(1, 2, 3)
        val sha256Message = Sha256.sha256(message)
        val signHash: String = privateKey.sign(sha256Message).signature()
        println(signHash)
    }
}