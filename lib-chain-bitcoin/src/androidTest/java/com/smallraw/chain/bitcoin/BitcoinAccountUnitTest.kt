package com.smallraw.chain.bitcoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

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

        val bitcoinKit = BitcoinKit(MainNet())


        val keyPair = bitcoinKit.generateKeyPair(
            PrivateKey.ofHex("74e5eb5e87a7eca6f3d9142fcbf26858fe75e57261df60208e97543222906b33")
        )

        val wifPrivateKey = bitcoinKit.getWIFPrivate(keyPair)
        val publicKey = keyPair.getPublicKey().getKey().toHex()
        val p2pshAddress = bitcoinKit.getP2PKHAddress(keyPair.getPublicKey()).toString()
        val p2shAddress = bitcoinKit.getP2SHAddress(keyPair.getPublicKey()).toString()

        Log.e(TAG, wifPrivateKey)
        Log.e(TAG, publicKey)
        Log.e(TAG, p2pshAddress)
        Log.e(TAG, p2shAddress)

        assertEquals(wifPrivateKey, "L18wo72G3Y4tcp8BqJEa6uqDCH6VbsewmYGkx54nsZhs6kmt9h5F")
        assertEquals(
            publicKey,
            "02e696c8a8d35a1c5f0e6a1f345424c34ed39f0e50195d7183cdf45cd237b2b96f"
        )
        assertEquals(p2pshAddress, "14nZeETvctxC6w3gdJMGL6ZtvDq79JtPhL")
    }
}