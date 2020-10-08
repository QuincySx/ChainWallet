package com.smallraw.chain.bitcoin.transaction.script

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoin.network.TestNet
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScriptInputUnitTest {
    @Test
    fun test_p2pk() {
        val scriptInputBytes =
            ScriptInput.fromScriptBytes("47304402204ccfa5358d3a7d2c8b07a31f7bc8883119205c3979593dc36dee98e8fb89af2602205dbb0f07c0b6d3a99bdf706e58ed41cea273901e804476f3833f6d9ca133426c01".hexToByteArray())
        Log.e("script unit test", scriptInputBytes.getUnmalleableBytes()?.toHex() ?: "")

        val scriptOutputBytes =
            ScriptOutput.fromScriptBytes("2102004a23684b6e12441ac4c913775f4f74584c48a9167d2fb65da6a2ddc9852761ac".hexToByteArray())

        Assert.assertEquals(scriptOutputBytes is ScriptOutputP2PK, true)
        Assert.assertEquals(
            scriptOutputBytes?.getAddress(TestNet())?.address,
            "mnWdTeiVk42nUX7MVzJmvMP9SAznNqpHuj"
        )
    }

    @Test
    fun test_p2pkh() {
        val scriptInputBytes =
            ScriptInput.fromScriptBytes("473044022035a8a8e565806a8381ab618e6ee22f2d332ece0a2d85135f04c479df40a40632022045091760888c79b997c39b6a383c8957826f38a452461a69ed35975f951f754801210376e202ed0977f1f4d58809fcbafa6a6153f11916e276aa7557e3845d9f84157d".hexToByteArray())
        Log.e("script unit test", scriptInputBytes.getUnmalleableBytes()?.toHex() ?: "")

        val scriptOutputBytes =
            ScriptOutput.fromScriptBytes("76a9147febbc0b56d6893133a2249245b07c0a2f5415e788ac".hexToByteArray())

        Assert.assertEquals(scriptOutputBytes is ScriptOutputP2PKH, true)
        Assert.assertEquals(
            scriptOutputBytes?.getAddress(TestNet())?.address,
            "msBLWYGuGGgpWxbao7BRXcCKaPLKgfknTq"
        )
    }

    @Test
    fun test_p2sh_multi() {
        // 6415d3688927056fccd98499924ecae15151244c111e480ded81e1800d2eaf30

        val scriptInputBytes =
            ScriptInput.fromScriptBytes("1600142cb7298f2030173e3bfa281bb812ac6fc68e0875".hexToByteArray())
        Log.e("script unit test", scriptInputBytes.getUnmalleableBytes()?.toHex() ?: "")

        val scriptOutputBytes =
            ScriptOutput.fromScriptBytes("a914673e8a29d83fd3b755b840dacd9212a561dbeecb87".hexToByteArray())

        Assert.assertEquals(scriptOutputBytes is ScriptOutputP2SH, true)
        Assert.assertEquals(
            scriptOutputBytes?.getAddress(TestNet())?.address,
            "2N2f8WNpnQm5czXFJ3j3YiEqnNqjPvNrcfs"
        )
    }

    @Test
    fun test_op_return() {
        val scriptOutputBytes =
            ScriptOutput.fromScriptBytes("6a4032366536343865643839356132653434643766346363373162323963346339356163636135353238383639326436636632363963633462326666643039363936".hexToByteArray())

        Assert.assertEquals(scriptOutputBytes is ScriptOutputOpReturn, true)
        Assert.assertEquals(
            scriptOutputBytes?.getAddress(TestNet())?.address,
            ""
        )
    }
}