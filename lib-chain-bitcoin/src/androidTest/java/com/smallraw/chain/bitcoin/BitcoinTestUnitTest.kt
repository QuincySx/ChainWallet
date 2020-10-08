package com.smallraw.chain.bitcoin

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BitcoinTestUnitTest {

    @Test
    fun create_key() {
        val privateKey =
            Bitcoin.PrivateKey.ofHex("74e5eb5e87a7eca6f3d9142fcbf26858fe75e57261df60208e97543222906b33")
        val keyPair = Bitcoin.KeyPair(privateKey)

        val private = keyPair.getPrivateKey().getKey().toHex()
        val public = keyPair.getPublicKey().getKey().toHex()
        val compress = keyPair.isCompress()

        val sign = keyPair.getPrivateKey().sign("010203".hexToByteArray())
        val signHex = sign.signature().toHex()
        print(signHex)
    }

}