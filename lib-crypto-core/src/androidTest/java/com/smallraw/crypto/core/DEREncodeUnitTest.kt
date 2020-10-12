package com.smallraw.crypto.core

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.crypto.core.crypto.DEREncode
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DEREncodeUnitTest {
    @Test
    fun test_der_encode() {
        val hexToBytes =
            "753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f42bb43fc9c6a3877e6aa1312059f1c4a9d376cf21c750c30b09fc2be7096be3e3".hexToByteArray()

        val toByteArray = DEREncode.sigToDer(hexToBytes)

        Log.e("der encode", toByteArray.toHex())

        Assert.assertEquals(
            toByteArray.toHex(),
            "30440220753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f402202bb43fc9c6a3877e6aa1312059f1c4a9d376cf21c750c30b09fc2be7096be3e3"
        )
    }

    @Test
    fun test_der_decode() {
        val hexToBytes =
            "30440220753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f402202bb43fc9c6a3877e6aa1312059f1c4a9d376cf21c750c30b09fc2be7096be3e3".hexToByteArray()

        val toByteArray = DEREncode.derToSig(hexToBytes)

        Log.e("der encode", toByteArray.toHex())

        Assert.assertEquals(
            toByteArray.toHex(),
            "753fa0fed5c3367e1e868ceca970c3d20825c4237790efdd25c78c917f7090f42bb43fc9c6a3877e6aa1312059f1c4a9d376cf21c750c30b09fc2be7096be3e3"
        )
    }
}