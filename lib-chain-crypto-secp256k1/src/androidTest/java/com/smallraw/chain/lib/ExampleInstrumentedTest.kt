package com.smallraw.chain.lib

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.crypto.Secp256K1
import com.smallraw.chain.lib.util.hexToBytes
import com.smallraw.chain.lib.util.toHex
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.mylibrary.test", appContext.packageName)
    }

    @Test
    fun addition_isCorrect_a() {
        val publicKey = Secp256K1.createPublicKey(
            "cad4e6e37c2e767ce74822174c8ca64b660754e5f65381dc8eae4fb552f17d84".hexToBytes(),
            true
        )
        //4abbf24c3ca4226e0f78fcadbdb1cfcb6dde06a88076ed9712f2bd89680abfbd6d7c63ee3223fa0821fac4899eb021d457ebd35568a8bbce5ed5a42422c474653
        val publicKeyHex = publicKey?.toHex()
        assertEquals(publicKeyHex, "01")
    }
}