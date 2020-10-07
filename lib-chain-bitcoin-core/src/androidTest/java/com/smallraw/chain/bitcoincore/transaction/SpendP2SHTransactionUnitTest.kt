package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.address.P2PKHAddress
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.lib.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SpendP2SHTransactionUnitTest {

    @Test
    fun spend_p2pkh_to_p2pkh() {
        val network = MainNet()

        val priv1 =
            PrivateKey("471817cfb2eedbc9a3e10b89d5fc3e8a5cdbc13fe58f08c6d9013df5060b2a3c".hexToByteArray())
        val priv2 =
            PrivateKey("71516154a21724d57fdcdb242cca34a324ffff9b3e4befcc7f375bc5e896250f".hexToByteArray())

        val lockScript =
            Script("522103d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e7302102d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd2103aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d953ae".hexToByteArray())

        val txin =
            Transaction.Input(
                "7a06ea98cd40ba2e3288262b28638cec5337c1456aaf5eedc8e9e5a20f062bdf".hexToByteArray(),
                0,
            )

        val payeeAddress = P2PKHAddress(network, address = "1Ce8WxgwjarzLtV6zkUGgdwmAe5yjHoPXX")

        val txout1 = Transaction.Output(4999950000, payeeAddress.lockScript())

        val tx = Transaction(arrayOf(txin), arrayOf(txout1))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest = TransactionSerializer.hashForSignature(tx, 0, lockScript)
        val sig1 = priv1.sign(txDigest)
        val sig2 = priv2.sign(txDigest)

        txin.script = Script(Chunk { OP_0 },
            ChunkData { sig1.signature() },
            ChunkData { sig2.signature() },
            ChunkData { lockScript.scriptBytes })

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "0200000001df2b060fa2e5e9c8ed5eaf6a45c13753ec8c63282b2688322eba40cd98ea067a0000000000ffffffff01b02e052a010000001976a9147faf0c785828c1f87fca32ef071066f60ea100d188ac00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "0200000001df2b060fa2e5e9c8ed5eaf6a45c13753ec8c63282b2688322eba40cd98ea067a00000000fdfd0000483045022100afc3420e8fb5264a0b8064c9f831aa221c677a398ac4dcf4c4bf6c13df64767d022026b888843407c749aa29a530912ffb92a893f0e2f9fe5486516b7184b1ce251c014730440220502aa4630a50c7257546b245959751da20ce133ce273708955f640c10023a7d50220400c8ba6e873ed1f772e234efe178a61d6d6124ebbd01923a2bf29c81a029229014c69522103d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e7302102d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd2103aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d953aeffffffff01b02e052a010000001976a9147faf0c785828c1f87fca32ef071066f60ea100d188ac00000000".hexToByteArray()
        )
    }
}