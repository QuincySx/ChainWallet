package com.smallraw.crypto

import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import com.smallraw.crypto.core.util.timeDiff
import com.smallraw.lib.crypto.Secp256K1
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class Secp256k1Test {
    private val TAG = "ExampleInstrumentedTest"

    @Test
    fun create_public_key() {
        timeDiff {
            val privateKey =
                "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexToByteArray()
            start("is run create PublicKey")

            val publicKey = Secp256K1.createPublicKey(privateKey, false)

            pause()

            val publicKeyHex = publicKey.toHex()

            end()
            println(publicKeyHex)
            assertEquals(
                publicKeyHex,
                "04bfbfc331f426f68032cd95e18b6ad71b20efdee52e07685362fdadc0e56b876a26a670fb340ff6ef468d937e3757fc5f93f4d67626651a086e99ffe741bc3a4f"
            )
        }
    }

    @Test
    fun sign() {
        val privateKey =
            "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexToByteArray()
        val message = "abcd".hexToByteArray()

        timeDiff {
            start("is run sign")
            val sign = Secp256K1.sign(privateKey, message)
            end()

            assertEquals(
                sign.toHex(),
                "753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f42bb43fc9c6a3877e6aa1312059f1c4a9d376cf21c750c30b09fc2be7096be3e3"
            )
        }
    }

    @Test
    fun sign1() {
        val privateKey =
            "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexToByteArray()
        val message =
            "179980f6862aedb22205ac97c8af29c77e25d02e189b52926bb1d93796bb3c94".hexToByteArray()

        timeDiff {
            start("is run sign")
            val sign = Secp256K1.sign(privateKey, message)
            end()

            assertEquals(
                sign.toHex(),
                "db80f176c04b68e1c54df3265d3031dd59b716ee7945d1b6e4ce8feb7250965960bda4a52bdec99968e5fa69cdb1005818505f153b164741c6854920e9c92d7d"
            )
        }
    }

    @Test
    fun verify() {
        val privateKey =
            "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexToByteArray()
        val publicKey = Secp256K1.createPublicKey(privateKey)
        val message = "abcd".hexToByteArray()
        val sign = Secp256K1.sign(privateKey, message)

        timeDiff {
            start("is run verify")
            val verify = Secp256K1.verify(publicKey, sign, message)
            end()

            assertEquals(verify, true)
        }
    }

    @Test
    fun verify1() {
        val privateKey =
            "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexToByteArray()
        val publicKey = Secp256K1.createPublicKey(privateKey)
        val message =
            "179980f6862aedb22205ac97c8af29c77e25d02e189b52926bb1d93796bb3c94".hexToByteArray()
        val sign = Secp256K1.sign(privateKey, message)

        timeDiff {
            start("is run verify")
            val verify = Secp256K1.verify(publicKey, sign, message)
            end()

            assertEquals(verify, true)
        }
    }
}