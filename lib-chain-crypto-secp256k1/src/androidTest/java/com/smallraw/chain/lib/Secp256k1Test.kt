package com.smallraw.chain.lib

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.crypto.Secp256K1
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import com.smallraw.chain.lib.extensions.toHex
import com.smallraw.chain.lib.util.timeDiff

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Secp256k1Test {
    private val TAG = "ExampleInstrumentedTest"

    @Test
    fun create_public_key() {
        timeDiff {
            val privateKey =
                "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexStringToByteArray()!!
            start("is run create PublicKey")

            val publicKey = Secp256K1.createPublicKey(privateKey)

            pause()

            val publicKeyHex = publicKey?.toHex()

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
            "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexStringToByteArray()!!
        val message = "abcd".hexStringToByteArray()!!

        timeDiff {
            start("is run sign")
            val sign = Secp256K1.sign(privateKey, message)
            end()

            assertEquals(
                sign!!.toHex(),
                "753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f4d44bc036395c7881955ecedfa60e3b54e7380dc4e7f7dd30b5d632a5c6ca5d5e"
            )
        }
    }

    @Test
    fun verify() {
        val privateKey =
            "55a51098556a373bc53ccf7b797278bbeab2219e8a281814cc0622601bd6a55d".hexStringToByteArray()!!
        val publicKey = Secp256K1.createPublicKey(privateKey)!!
        val message = "abcd".hexStringToByteArray()!!
//        val sign = Secp256K1.sign(privateKey, message)!!
        val sign =
            "753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f4d44bc036395c7881955ecedfa60e3b54e7380dc4e7f7dd30b5d632a5c6ca5d5e".hexStringToByteArray()!!

        timeDiff {
            start("is run verify")
            val verify = Secp256K1.verify(publicKey, sign, message)
            end()

            assertEquals(verify, true)
        }
    }
}