package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.address.P2WPKHAddress
import com.smallraw.chain.bitcoincore.address.P2WSHAddress
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.lib.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SpendP2WSHTransactionUnitTest {
    @Test
    fun test_spend_p2wsh_to_p2wpkh() {
        val network = TestNet()

        val priv1 =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())
        val priv2 =
            PrivateKey("69b33e2ee0f0cc5620df24fc804f338c8735098953387151e01903c0dada0661".hexToByteArray())

        val paymentP2SHLockScript = Script(
            Chunk { OP_1 },
            ChunkData { priv1.getPublicKey().getKey() },
            ChunkData { priv2.getPublicKey().getKey() },
            Chunk { OP_2 },
            Chunk { OP_CHECKMULTISIG })

        val paymentAddress = P2WSHAddress(network, script = paymentP2SHLockScript)
        val payeeAddress =
            P2WPKHAddress(network, address = "tb1qtstf97nhk2gycz7vl37esddjpxwt3ut30qp5pn")

        val txinPrevAmount = 1000000L
        val txin = Transaction.Input(
            "2042195c40a92353f2ffe30cd0df8d177698560e81807e8bf9174a9c0e98e6c2".hexToByteArray(),
            0
        )

        val txOut1 = Transaction.Output(10000, payeeAddress.lockScript())
        // change 找零
        val txOut2 = Transaction.Output(980000, paymentAddress.lockScript())

        val tx = Transaction(arrayOf(txin), arrayOf(txOut1, txOut2))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest =
            TransactionSerializer.hashForWitnessSignature(
                tx,
                0,
                paymentP2SHLockScript,
                txinPrevAmount
            )
        val sig1 = priv1.sign(txDigest)

        tx.inputs[0].witness.setStack(0, Chunk { OP_0 }.toBytes())
        tx.inputs[0].witness.setStack(1, sig1.signature())
        tx.inputs[0].witness.setStack(2, paymentP2SHLockScript.scriptBytes)

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false, false),
            "0200000001c2e6980e9c4a17f98b7e80810e569876178ddfd00ce3fff25323a9405c1942200000000000ffffffff0210270000000000001600145c1692fa77b2904c0bccfc7d9835b2099cb8f17120f40e0000000000220020c4b3ccbc954a24abfd903289f411c8af1b1f4b246abb818fae66f4450b45b20800000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "02000000000101c2e6980e9c4a17f98b7e80810e569876178ddfd00ce3fff25323a9405c1942200000000000ffffffff0210270000000000001600145c1692fa77b2904c0bccfc7d9835b2099cb8f17120f40e0000000000220020c4b3ccbc954a24abfd903289f411c8af1b1f4b246abb818fae66f4450b45b208030100483045022100d0cdcf393110b459e12f5235e2e946c6de8a2be5495b8c446306c8c1f3afb0830220347c74ee627ba00ee8ccdcc266ba31cbf50e9262a78b4d092bb68809d1158c59014751210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e2103aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd1752ae00000000".hexToByteArray()
        )
    }
}