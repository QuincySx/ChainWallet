package com.smallraw.wallet.hd

import android.util.Log
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test


class HDKeyDerivationUnitTest {

    @Test
    fun test_derived_hardened() {
        val RootKey =
            HDKeyDerivation.createRootKey("c01ea67cf34f5130e1f1b43a2f01591c251f01de04e93506c78f21737594c6d2ac52510fc3257f0184daf854042419c084d576da5fafa85fe81a5d5e770da1ea".hexToByteArray())

        Assert.assertEquals(RootKey.getPrivKeyBytes()?.toHex(),"a3834e9620dcc4359d25db322206bd2a2105e1e4f0d742d943237903eaea322d")
        Assert.assertEquals(RootKey.getPubKey()?.toHex(),"033871f247ab6460a4b49526280093b446b2bc2a7f4dd16b03e54df3bb55223a58")

        val deriveChildKey1 = HDKeyDerivation.deriveChildKey(RootKey, 44, true)
        val deriveChildKey2 = HDKeyDerivation.deriveChildKey(deriveChildKey1, 0, true)
        val deriveChildKey3 = HDKeyDerivation.deriveChildKey(deriveChildKey2, 0, true)
        val deriveChildKey4 = HDKeyDerivation.deriveChildKey(deriveChildKey3, 0, false)
        val deriveChildKey5 = HDKeyDerivation.deriveChildKey(deriveChildKey4, 0, false)

        deriveChildKey1.getPubKey()?.toHex()?.let { Log.e("===key derived===", it) }
        Assert.assertEquals(deriveChildKey5.getPubKey()?.toHex(),"03bfad316f1aec2c385d5b2b83a7c27abaf61e6a4c262cd15acb835e410bee12c5")
    }

    @Test
    fun test_derived() {
        val RootKey =
            HDKeyDerivation.createRootKey("c01ea67cf34f5130e1f1b43a2f01591c251f01de04e93506c78f21737594c6d2ac52510fc3257f0184daf854042419c084d576da5fafa85fe81a5d5e770da1ea".hexToByteArray())

        Assert.assertEquals(RootKey.getPrivKeyBytes()?.toHex(),"a3834e9620dcc4359d25db322206bd2a2105e1e4f0d742d943237903eaea322d")
        Assert.assertEquals(RootKey.getPubKey()?.toHex(),"033871f247ab6460a4b49526280093b446b2bc2a7f4dd16b03e54df3bb55223a58")

        val deriveChildKey = HDKeyDerivation.deriveChildKey(RootKey, 0, false)
        Assert.assertEquals(deriveChildKey.getPrivKeyBytes()?.toHex(),"395dfa96532c8fcdc523cd717ce7345bf93b75bba3290b166550fa6fd348cb5c")
        Assert.assertEquals(deriveChildKey.getPubKey()?.toHex(),"02631cfea739169bfd37a87371908cc7bca50862b1008298f5a5d2da8b6d6e0cd1")
    }
}