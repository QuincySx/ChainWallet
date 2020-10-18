package com.smallraw.chain.ethereum.abi

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigInteger

@RunWith(AndroidJUnit4::class)
class AbiUnitTest {

    @Test
    fun test_encode1() {
        val encodeParam1 = Abi.encodeParam("uint256", 2345675643).toHex()
        Assert.assertEquals(
            encodeParam1,
            "000000000000000000000000000000000000000000000000000000008bd02b7b"
        )

        val encodeParam2 = Abi.encodeParam("bytes32", "0xdf3234".hexToByteArray()).toHex()
        Assert.assertEquals(
            encodeParam2,
            "df32340000000000000000000000000000000000000000000000000000000000"
        )

        val encodeParam3 = Abi.encodeParam("bytes", "0xdf3234".hexToByteArray()).toHex()
        Assert.assertEquals(
            encodeParam3,
            "00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000003df32340000000000000000000000000000000000000000000000000000000000"
        )

        val encodeParam4 = Abi.encodeParam(
            "bytes32[]",
            listOf("0xdf3234".hexToByteArray(), "0xfdfd".hexToByteArray())
        ).toHex()
        Assert.assertEquals(
            encodeParam4,
            "00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000002df32340000000000000000000000000000000000000000000000000000000000fdfd000000000000000000000000000000000000000000000000000000000000"
        )
    }

    @Test
    fun test_encode2() {
        val encodeParam1 =
            Abi.encodeParams(arrayOf("uint256", "string"), arrayOf(2345675643, "Hello!%")).toHex()
        Assert.assertEquals(
            encodeParam1,
            "000000000000000000000000000000000000000000000000000000008bd02b7b0000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000000748656c6c6f212500000000000000000000000000000000000000000000000000"
        )

        val encodeParam2 = Abi.encodeParams(
            arrayOf("uint8[]", "bytes32"),
            arrayOf(arrayOf("34", "434"), "0x324567fff".hexToByteArray())
        ).toHex()
        Assert.assertEquals(
            encodeParam2,
            "0000000000000000000000000000000000000000000000000000000000000040324567ff000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000002200000000000000000000000000000000000000000000000000000000000001b2"
        )
    }

    @Test
    fun test_decode1() {
        val decodeParam1 = Abi.decodeParam(
            "0x0000000000000000000000000000000000000000000000000000000000000010".hexToByteArray(),
            "uint256"
        ) as BigInteger
        Assert.assertEquals(
            decodeParam1.toInt(),
            16
        )

        val encodeParam2 = Abi.decodeParam(
            "0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000848656c6c6f212521000000000000000000000000000000000000000000000000".hexToByteArray(),
            "string"
        )
        Assert.assertEquals(
            encodeParam2,
            "Hello!%!"
        )
    }

    @Test
    fun test_decode2() {
        val decodeParam1 = Abi.decodeParams(
            "0x000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000ea000000000000000000000000000000000000000000000000000000000000000848656c6c6f212521000000000000000000000000000000000000000000000000".hexToByteArray(),
            arrayOf("string", "uint256")
        )
        Assert.assertEquals(
            decodeParam1[0],
            "Hello!%!"
        )

        Assert.assertEquals(
            (decodeParam1[1] as BigInteger).toInt(),
            234
        )

        val encodeParam2 = Abi.decodeParam(
            "0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000848656c6c6f212521000000000000000000000000000000000000000000000000".hexToByteArray(),
            "string"
        )
        Assert.assertEquals(
            encodeParam2,
            "Hello!%!"
        )
    }
}