package com.smallraw.chain.lib

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import com.smallraw.chain.lib.extensions.toHex

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BitcoinAccountUnitTest {
    private val TAG = "BitcoinAccountUnitTest"

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.mylibrary.test", appContext.packageName)
//    }

    @Test
    fun create_bitcoin_account() {
        val privateKey =
            "74e5eb5e87a7eca6f3d9142fcbf26858fe75e57261df60208e97543222906b33".hexStringToByteArray()!!
        val bitcoinAccount = BitcoinAccount(
            Secp256k1PrivateKey(
                privateKey
            ), testNet = false)
        val wifPrivateKey = bitcoinAccount.getWifPrivateKey()
        val publicKey = bitcoinAccount.getPublicKey().format
        val address = bitcoinAccount.getAddress().getFormat()
        val rawAddress = bitcoinAccount.getAddress().getAddress().toHex()

        Log.e(TAG, wifPrivateKey)
        Log.e(TAG, publicKey)
        Log.e(TAG, address)
        Log.e(TAG, rawAddress)

        assertEquals(wifPrivateKey, "L18wo72G3Y4tcp8BqJEa6uqDCH6VbsewmYGkx54nsZhs6kmt9h5F")
        assertEquals(publicKey, "02e696c8a8d35a1c5f0e6a1f345424c34ed39f0e50195d7183cdf45cd237b2b96f")
        assertEquals(rawAddress, "00298657ffb809d64076c585dd67fd80753d470619b05d72c3")
        assertEquals(address, "14nZeETvctxC6w3gdJMGL6ZtvDq79JtPhL")
    }
}