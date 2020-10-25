package com.smallraw.wallet.hd

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HDKeyDerivationUnitTest {

    @Test
    fun test_derived() {
        val RootKey =
            HDKeyDerivation.createRootKey("c01ea67cf34f5130e1f1b43a2f01591c251f01de04e93506c78f21737594c6d2ac52510fc3257f0184daf854042419c084d576da5fafa85fe81a5d5e770da1ea".hexToByteArray())
        val deriveChildKey1 = HDKeyDerivation.deriveChildKey(RootKey, 0, false)
//        val deriveChildKey1 = HDKeyDerivation.deriveChildKey(RootKey, 44, true)
//        val deriveChildKey2 = HDKeyDerivation.deriveChildKey(deriveChildKey1, 0, true)
//        val deriveChildKey3 = HDKeyDerivation.deriveChildKey(deriveChildKey2, 0, true)
//        val deriveChildKey4 = HDKeyDerivation.deriveChildKey(deriveChildKey3, 0, false)
//        val deriveChildKey5 = HDKeyDerivation.deriveChildKey(deriveChildKey4, 0, false)

        Log.e("===key derived===",deriveChildKey1.getPubKey().toHex())
        Assert.assertEquals(deriveChildKey1.getPubKey().toHex(),"03bfad316f1aec2c385d5b2b83a7c27abaf61e6a4c262cd15acb835e410bee12c5")
    }

}