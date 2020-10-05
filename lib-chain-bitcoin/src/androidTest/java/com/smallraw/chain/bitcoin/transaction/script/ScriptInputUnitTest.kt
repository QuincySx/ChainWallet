package com.smallraw.chain.bitcoin.transaction.script

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.bitcoin.network.MainNet
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScriptInputUnitTest {
    @Test
    fun test_p2pkh_multi() {
        val scriptInputBytes =
            ScriptInput.fromScriptBytes("483045022100e6b72c1d4626f285a883a0ecf26b134fdbb15fe8b4d584affdf91cf64f3f2b1b022046eff1676b7ce555aa792ec22170dc2ebeb2901ed6f47384dd7ff32dff2281a3012103c178f3f59eb10ae875f851168507570059da5ee21009891f3d8606cbd5b00822".hexToByteArray())
        Log.e("script unit test", scriptInputBytes.getUnmalleableBytes().toString())

        val scriptOutputBytes =
            ScriptOutput.fromScriptBytes("76a914e569724d793afcb4ed90335e6ae8c552edcc711388ac".hexToByteArray())
        Log.e("script unit test", scriptOutputBytes?.getAddress(MainNet()).toString())
    }

    @Test
    fun test_p2sh_multi() {
        val scriptInputBytes =
            ScriptInput.fromScriptBytes("16001459222d6a86727f4f929ef6632e358cca17a82bc0".hexToByteArray())
        Log.e("script unit test", scriptInputBytes.getUnmalleableBytes().toString())

        val scriptOutputBytes =
            ScriptOutput.fromScriptBytes("a9141a1b9bb8c5ca4acbb5b17d6d99ae0b5a292d363487".hexToByteArray())
        Log.e("script unit test", scriptOutputBytes?.getAddress(MainNet()).toString())
    }
}