package com.smallraw.chain.lib

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.util.hexToBytes
import com.smallraw.chain.lib.util.toHex

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CryptoUnitTest {
    private val TAG = "ExampleInstrumentedTest"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.mylibrary.test", appContext.packageName)
    }

    @Test
    fun base58() {
        val date = "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToBytes()
        val encode = Base58.encode(date)
        assertEquals(encode, "JBj25V2ETjYro9YntCiSnVx2pp6AydWnpxF1wws1FXUF")
        val decode = Base58.decode(encode!!)
        assertArrayEquals(decode, date)
    }

    @Test
    fun base58_check() {
        val date = "abcd".hexToBytes()
        val encode = Base58.encodeCheck(date)
        assertEquals(encode, "2UZ1mCYWH")
        val decode = Base58.decodeCheck(encode!!)
        assertArrayEquals(decode, date)
    }

    @Test
    fun sha256() {
        val sha256 = Sha256.sha256("123".toByteArray())?.toHex()
        assertEquals(sha256, "A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3")
        val doubleSha256 =
            Sha256.doubleSha256("4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToBytes())
                ?.toHex()
        assertEquals(
            doubleSha256,
            "711EEE90564B715FC3A1DC2D39193AF058DFB3901D5EBD771F5AE87A951E9035"
        )
    }
}