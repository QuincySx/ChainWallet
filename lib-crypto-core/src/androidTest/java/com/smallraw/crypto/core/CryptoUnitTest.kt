package com.smallraw.crypto.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.crypto.core.crypto.*
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CryptoUnitTest {
    private val TAG = "ExampleInstrumentedTest"

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.mylibrary.test", appContext.packageName)
//    }

    @Test
    fun base58() {
        val date =
            "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToByteArray()
        val encode = Base58.encode(date)
        assertEquals(encode, "6CtHPVEH56orAaTL6M4UC6o8nF1f1Z5TPPQdGFWuzsqd")
        val decode = Base58.decode(encode)
        assertArrayEquals(decode, date)
    }

    @Test
    fun base58_check() {
        val date = "abcd".hexToByteArray()
        val encode = Base58.encodeCheck(date)
        assertEquals(encode, "2UZ1mCYWH")
        val decode = Base58.decodeCheck(encode)
        assertArrayEquals(decode, date)
    }

    @Test
    fun sha256() {
        val sha256 = Sha256.sha256("123".toByteArray()).toHex()
        assertEquals(sha256, "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3")
        val doubleSha256 =
            Sha256.doubleSha256("abcd".hexToByteArray()).toHex()
        assertEquals(
            doubleSha256,
            "179980f6862aedb22205ac97c8af29c77e25d02e189b52926bb1d93796bb3c94"
        )
    }

    @Test
    fun ripemd160() {
        val data = "abcd".hexToByteArray()
        val hash = Ripemd160.ripemd160(data).toHex()
        assertEquals(
            hash,
            "a21c2817130deaa1105afb3b858dbd219ee2da44"
        )
    }

    @Test
    fun sha3_256() {
        val data = "abcd".hexToByteArray()
        val hash = Sha3.sha256(data).toHex()
        assertEquals(
            hash,
            "0f1108bfb4ddb5cd6a8b05ad6dbc8244f0b0ef94cf77475a60a7bc952058425b"
        )

        val doubleHash = Sha3.doubleSha256(data).toHex()
        assertEquals(
            doubleHash,
            "0e55640509c955154be3b6f05f655b99d475199a0c4d044ef6f2bcbae4630d0c"
        )
    }

    @Test
    fun keccak_256() {
        val data = "abcd".hexToByteArray()
        val hash = Keccak.sha256(data).toHex()
        assertEquals(
            hash,
            "dbe576b4818846aa77e82f4ed5fa78f92766b141f282d36703886d196df39322"
        )

        val doubleHash = Keccak.doubleSha256(data).toHex()
        assertEquals(
            doubleHash,
            "d33e7c66e18c0118da21e9a35495306a28bb2e278781352ee93c962c19b47452"
        )
    }

    @Test
    fun hmac_sha2() {
        val sha256 = HmacSha2.sha256("Bitcoin seed".toByteArray(), "abcd".toByteArray()).toHex()
        assertEquals(sha256, "860f28d55c12f1162a3fd482c9da3c86ba8c7d772f2be6f94d030057ff9168b3")

        val sha512 = HmacSha2.sha512("Bitcoin seed".toByteArray(), "abcd".toByteArray()).toHex()
        assertEquals(
            sha512,
            "9ff7be0af575c24466b96190b97034972c781bf4204cf5531d6eece7b8ec942b8b5a71d9158eae9b6121c3f322a661e1d6c5362859d78b5c553525c30cc0e56e"
        )
    }
}