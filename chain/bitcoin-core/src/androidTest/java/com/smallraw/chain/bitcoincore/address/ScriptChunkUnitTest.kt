package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test


class ScriptChunkUnitTest {
    @Test
    fun test_script_form_chunks() {
        val scriptByScriptChunkOf = Script(
            ScriptChunk.of(OP_1),
            ScriptChunk.of("03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".hexToByteArray()),
            ScriptChunk.of("027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b16093".hexToByteArray()),
            ScriptChunk.of("027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc".hexToByteArray()),
            ScriptChunk.of(OP_3),
            ScriptChunk.of(OP_CHECKMULTISIG)
        )

        val scriptByChunk = Script(
            Chunk(OP_1),
            Chunk("03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".hexToByteArray()),
            Chunk("027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b16093".hexToByteArray()),
            Chunk("027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc".hexToByteArray()),
            Chunk(OP_3),
            Chunk(OP_CHECKMULTISIG)
        )

        val scriptByChunkInt = Script(
            Chunk(1),
            Chunk("03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".hexToByteArray()),
            Chunk("027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b16093".hexToByteArray()),
            Chunk("027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc".hexToByteArray()),
            Chunk(3),
            Chunk(OP_CHECKMULTISIG)
        )

        Assert.assertEquals(
            scriptByScriptChunkOf.scriptBytes.toHex(),
            "512103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af3270821027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b1609321027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc53ae"
        )

        Assert.assertEquals(
            scriptByChunk.scriptBytes.toHex(),
            "512103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af3270821027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b1609321027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc53ae"
        )

        Assert.assertEquals(
            scriptByChunkInt.scriptBytes.toHex(),
            "512103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af3270821027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b1609321027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc53ae"
        )
    }

    @Test
    fun test_chunks_decode_to_script() {
        val script =
            Script("512103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af3270821027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b1609321027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc53ae".hexToByteArray())

        Assert.assertArrayEquals(script.chunks[0].toBytes(), Chunk(OP_1).toBytes())
        Assert.assertArrayEquals(
            script.chunks[1].toBytes(),
            Chunk("03a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708".hexToByteArray()).toBytes()
        )
        Assert.assertArrayEquals(
            script.chunks[2].toBytes(),
            Chunk("027d25cf6f3e487ba665121d25fa75aaf68434ed191d1d3fa85fb21f2583b16093".hexToByteArray()).toBytes()
        )
        Assert.assertArrayEquals(
            script.chunks[3].toBytes(),
            Chunk("027e32f101858cac06d17d93eb04a7c50c45ff5684f3a6083901b9c3495e99cbdc".hexToByteArray()).toBytes()
        )
        Assert.assertArrayEquals(script.chunks[4].toBytes(), Chunk(OP_3).toBytes())
        Assert.assertArrayEquals(script.chunks[5].toBytes(), Chunk(OP_CHECKMULTISIG).toBytes())
    }
}