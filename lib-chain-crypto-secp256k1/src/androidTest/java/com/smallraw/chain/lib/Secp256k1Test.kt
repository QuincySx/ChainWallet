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
                "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexStringToByteArray()!!
            start("is run create PublicKey")

            val publicKey = Secp256K1.createPublicKey(privateKey)

            pause()

            val publicKeyHex = publicKey?.toHex()

            end()
            println(publicKeyHex)
            assertEquals(
                publicKeyHex,
                "045958b1c3debd87feb88c7a2cf471732d3fc1e9e2d9f43f1efb1497541cd800f475fc094940d088dfc6948429b0427b9ab520d8c062a4b55dd69a0ca920a5e937"
            )
        }
    }

    @Test
    fun sign() {
        val privateKey =
            "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexStringToByteArray()!!
        val message = "123".hexStringToByteArray()!!

        timeDiff {
            start("is run sign")
            Secp256K1.sign(privateKey, message)
            end()
        }
    }

    @Test
    fun verify() {
        val privateKey =
            "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexStringToByteArray()!!
        val publicKey = Secp256K1.createPublicKey(privateKey)!!
        val message = "123".hexStringToByteArray()!!
        val sign = Secp256K1.sign(privateKey, message)!!

        timeDiff {
            start("is run verify")
            val verify = Secp256K1.verify(publicKey, sign, message)
            end()

            assertEquals(verify, true)
        }
    }
}