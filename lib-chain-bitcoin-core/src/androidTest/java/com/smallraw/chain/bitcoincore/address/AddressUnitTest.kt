package com.smallraw.chain.bitcoincore.address

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.OP_1
import com.smallraw.chain.bitcoincore.script.OP_2
import com.smallraw.chain.bitcoincore.script.OP_CHECKMULTISIG
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.crypto.core.extensions.hexToByteArray
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressUnitTest {

    @Test
    fun test_p2sh_p2wsh_address() {
        val convert = AddressConverter.default(MainNet())
        val p2wpkhAddress = convert.convert(
            Script(
                Chunk(OP_0),
                Chunk("9592d601848d04b172905e0ddb0adde59f1590f1e553ffc81ddc4b0ed927dd73".hexToByteArray())
            )
        )
        System.err.println(p2wpkhAddress.getType())
    }

    @Test
    fun test_p2pkh_address() {
        val convert = AddressConverter.default(TestNet())
        val p2wpkhAddress =
            convert.convert(
                PublicKey("0320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e".hexToByteArray()),
                ScriptType.P2PKH
            )

        Assert.assertEquals(p2wpkhAddress.toString(), "mma38oKsYVVvPBZNcq7Spssrup4nCzqtdw")

        val convertAddress =
            convert.convert("mma38oKsYVVvPBZNcq7Spssrup4nCzqtdw")
        Assert.assertEquals(
            convertAddress.getType(),
            Address.AddressType.P2PKH
        )
    }

    @Test
    fun test_p2sh_address() {
        val convert = AddressConverter.default(TestNet())

        val script = Script(
            Chunk(OP_1),
            Chunk("0320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e".hexToByteArray()),
            Chunk("03aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd17".hexToByteArray()),
            Chunk(OP_2),
            Chunk(OP_CHECKMULTISIG)
        )

        val p2wpkhAddress = convert.convert(
            script, ScriptType.P2SH
        )

        Assert.assertEquals(
            p2wpkhAddress.toString(),
            "2N58MTyhMQKiFMiujtiTCkLrE2sZnXM8hEm"
        )

        val convertAddress =
            convert.convert("2N58MTyhMQKiFMiujtiTCkLrE2sZnXM8hEm")
        Assert.assertEquals(
            convertAddress.getType(),
            Address.AddressType.P2SH
        )
    }

    @Test
    fun test_p2wpkh_address() {
        val convert = AddressConverter.default(TestNet())
        val p2wpkhAddress =
            convert.convert(
                PublicKey("0320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e".hexToByteArray()),
                ScriptType.P2WPKH
            )

        Assert.assertEquals(p2wpkhAddress.toString(), "tb1qgfnqkrskfutlllfkz2whvgtrx4d6c6064wpc0t")

        val convertAddress =
            convert.convert("tb1qgfnqkrskfutlllfkz2whvgtrx4d6c6064wpc0t")
        Assert.assertEquals(
            convertAddress.getType(),
            Address.AddressType.P2WPKHV0
        )
    }

    @Test
    fun test_p2wsh_address() {
        val convert = AddressConverter.default(TestNet())

        val script = Script(
            Chunk(OP_1),
            Chunk("0320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e".hexToByteArray()),
            Chunk("03aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd17".hexToByteArray()),
            Chunk(OP_2),
            Chunk(OP_CHECKMULTISIG)
        )

        val p2wpkhAddress = convert.convert(
            script, ScriptType.P2WSH
        )

        Assert.assertEquals(
            p2wpkhAddress.toString(),
            "tb1qcjeue0y4fgj2hlvsx2ylgywg4ud37jeyd2acrrawvm6y2z69kgyq7nys5p"
        )

        val convertAddress =
            convert.convert("tb1qcjeue0y4fgj2hlvsx2ylgywg4ud37jeyd2acrrawvm6y2z69kgyq7nys5p")
        Assert.assertEquals(
            convertAddress.getType(),
            Address.AddressType.P2WSHV0
        )
    }
}