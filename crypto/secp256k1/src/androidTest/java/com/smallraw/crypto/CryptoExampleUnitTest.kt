package com.smallraw.crypto

import android.util.Log
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

class CryptoExampleUnitTest {
    private val TAG = "ExampleInstrumentedTest"

    @Test
    fun test_create_private_key() {
        val privateKey = Secp256K1.createPrivateKey()

        Log.e(TAG, privateKey.toHex())
        assertEquals(privateKey.size, 32)
    }

    @Test
    fun addition_isCorrect_a() {
        timeDiff {
            start()

            val publicKey = Secp256K1.createPublicKey(
                "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToByteArray(),
                false
            )
            //45958b1c3debd87feb88c7a2cf471732d3fc1e9e2d9f43f1efb1497541cd800f475fc094940d088dfc6948429b0427b9ab520d8c062a4b55dd69a0ca920a5e937

            pause()

            val publicKeyHex = publicKey.toHex()

            end()
            println(publicKeyHex)
            assertEquals(
                publicKeyHex,
                "045958b1c3debd87feb88c7a2cf471732d3fc1e9e2d9f43f1efb1497541cd800f475fc094940d088dfc6948429b0427b9ab520d8c062a4b55dd69a0ca920a5e937"
            )
        }
    }

    @Test
    fun test_hex() {
        timeDiff {
            start()
            val bytes =
                "045958b1c3debd87feb88c7a2cf471732d3fc1e9e2d9f43f1efb1497541cd800f475fc094940d088dfc6948429b0427b9ab520d8c062a4b55dd69a0ca920a5e937".hexToByteArray()
            pause()
            val hexString = bytes.toHex()
            end()
            Log.e("================", hexString)
            val text = byteArrayOf(4, 6, 76).toHex()
            Log.e("================", "====================================")
            Log.e("================", text)
            Log.e("================", "====================================")

            assertEquals(text, "04064c")
        }
    }
}