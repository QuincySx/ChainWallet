package com.smallraw.chain.bitcoincore.script

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.crypto.Bech32Segwit
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.lib.extensions.plus
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScriptAddressUnitTest {

    @Test
    fun test_p2pkh_address() {
        val testNet = TestNet()
        // 公钥 hash160
        val publicKey =
            "03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".toByteArray()
        val script = testNet.addressVersion + Ripemd160.hash160(publicKey)

        val address = Base58.encodeCheck(script)

        Assert.assertEquals(address, "mjAqu9BtZ5JGBLCnhyVcZefgKTuXjRqjLV")
    }

    @Test
    fun test_p2sh_multi_address() {
        val testNet = TestNet()

        // multi p2sh redeem script
        val redeemScript = Script(
            Chunk { OP_1 },
            ChunkData { "03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".hexToByteArray() },
            ChunkData { "027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b16093".hexToByteArray() },
            ChunkData { "027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc".hexToByteArray() },
            Chunk { OP_3 },
            Chunk { OP_CHECKMULTISIG }
        )

        val script = testNet.addressScriptVersion + Ripemd160.hash160(redeemScript.scriptBytes)

        val address = Base58.encodeCheck(script)

        Assert.assertArrayEquals(
            redeemScript.scriptBytes,
            "512103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af3270821027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b1609321027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc53ae".hexToByteArray()
        )
        Assert.assertEquals(address, "2NDjq2yVbP1MBMsVZXKxDkGNTVEFGvm6tMG")
    }

    @Test
    fun test_p2wpkh_address() {
        val testNet = TestNet()
        // 公钥 hash160
        val publicKey =
            "03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".toByteArray()
        val hashKey = Ripemd160.hash160(publicKey)

        val witnessScript = Bech32Segwit.convertBits(hashKey, 0, hashKey.size, 8, 5, true)
        val address = Bech32Segwit.encode(testNet.addressSegwitHrp, 0 + witnessScript)

        Assert.assertEquals(address, "tb1q9qfds7x9uxjj9hh7hnml9p205xjtu7yzk4qlvn")
    }

    @Test
    fun test_p2wsh_address() {
        val testNet = TestNet()
        // 公钥 hash160
        // multi p2sh redeem script
        val redeemScript = Script(
            Chunk { OP_1 },
            ChunkData { "03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".hexToByteArray() },
            ChunkData { "027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b16093".hexToByteArray() },
            ChunkData { "027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc".hexToByteArray() },
            Chunk { OP_3 },
            Chunk { OP_CHECKMULTISIG }
        )
        val hashKey = Sha256.sha256(redeemScript.scriptBytes)

        val witnessScript = Bech32Segwit.convertBits(hashKey, 0, hashKey.size, 8, 5, true)
        val address = Bech32Segwit.encode(testNet.addressSegwitHrp, 0 + witnessScript)

        Assert.assertEquals(address, "tb1qpc0fgj7djrytehdp7qpv63gz0lzcwxl5ycsj0wxe8nhnfy2v2vks6h0gsc")
    }
}