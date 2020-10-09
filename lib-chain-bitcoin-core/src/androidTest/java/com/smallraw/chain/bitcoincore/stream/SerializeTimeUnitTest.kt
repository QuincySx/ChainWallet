package com.smallraw.chain.bitcoincore.stream

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.ChunkData
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.transaction.Transaction
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.util.timeDiff
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SerializeTimeUnitTest {

    @Test
    fun test_serialize_time() {
        val network = MainNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("471817cfb2eedbc9a3e10b89d5fc3e8a5cdbc13fe58f08c6d9013df5060b2a3c".hexToByteArray())
        val priv2 =
            PrivateKey("71516154a21724d57fdcdb242cca34a324ffff9b3e4befcc7f375bc5e896250f".hexToByteArray())

        // P2SH 的多签脚本
        val lockScript =
            Script("522103d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e7302102d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd2103aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d953ae".hexToByteArray())

        val txin =
            Transaction.Input(
                "7a06ea98cd40ba2e3288262b28638cec5337c1456aaf5eedc8e9e5a20f062bdf".hexToByteArray(),
                0,
            )

        val payeeAddress = convert.convert("1Ce8WxgwjarzLtV6zkUGgdwmAe5yjHoPXX")

        val txout1 = Transaction.Output(4999950000, payeeAddress.lockScript())

        val tx = Transaction(arrayOf(txin), arrayOf(txout1))

        System.err.println("Transaction  $tx")

        timeDiff {
            start("Transaction")
            val serialize = TransactionSerializer.serialize(tx)
            pause("Serialize")
            TransactionSerializer.deserialize(serialize)
            pause("Deserialize")
            tx.copy()
            pause("Copy Serialize")
            val txDigest = TransactionSerializer.hashForSignature(tx, 0, lockScript)
            pause("Hash For Signature")
            val sig1 = priv1.sign(txDigest)
            pause("Sign1")
            val sig2 = priv2.sign(txDigest)
            pause("Sign2")
            Script(Chunk { OP_0 },
                ChunkData { sig1.signature() },
                ChunkData { sig2.signature() },
                ChunkData { lockScript.scriptBytes }).scriptBytes
            pause("Script Serialize")
            end()
        }

    }
}