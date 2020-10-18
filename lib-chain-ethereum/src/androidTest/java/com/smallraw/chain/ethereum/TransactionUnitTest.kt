package com.smallraw.chain.ethereum

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.ethereum.extensions.toHexPrefix
import com.smallraw.chain.ethereum.network.MainNet
import com.smallraw.chain.ethereum.transaction.Transaction
import com.smallraw.chain.ethereum.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.hexToByteArray
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigInteger

@RunWith(AndroidJUnit4::class)
class TransactionUnitTest {
    @Test
    fun test_transaction() {
        val privateKey =
            PrivateKey.ofHex("89afc24b157548633b6e54e4e7e6f00096cfb0e750854914472fc6571306e849")
        val publicKey = privateKey.getPublicKey()
        val address = publicKey.getAddress()

        val transaction = Transaction(
            nonce = 0,
            gasPrice = 1,
            gasLimit = 1,
            to = address,
            value = BigInteger.valueOf(1000000000000000000)
        )

        val sha256 = TransactionSerializer.hashForSignature(transaction, MainNet())
        Log.e("transaction sha256 unit test: ", sha256.toHexPrefix())
        val sign = privateKey.sign(sha256)
        transaction.signature = sign

        Log.e(
            "transaction unit test: ",
            TransactionSerializer.serialize(transaction, MainNet()).toHexPrefix()
        )
        Assert.assertEquals(
            TransactionSerializer.serialize(transaction, MainNet()).toHexPrefix(),
            "0xf865800101944626f9ea04267a7ff2904f9e6808bd7042cff858880de0b6b3a76400008026a0885dd1e66a0943368d5e759f6a4e3339b15e9f671eaeb191fb1e45d292a7bc6ba069f5637e1cd1b93e0a47d17edee30e85cb22dc1b01b0b8eb25da9613eb66ca05"
        )

        val deserialize =
            TransactionSerializer.deserialize("0xf8678080825208944626f9ea04267a7ff2904f9e6808bd7042cff8588898a7d9b8314c00008026a0f2a9ab56cb36ec4b0f788e2b1eda0550b7b4c3f95a8ea366c0319c12db4addf9a04b6cfbafe57903cf85acd0667c2482e5db3b4a38dc908b906c080e5e74309bc3".hexToByteArray())

        Log.e("transaction unit test: ", deserialize.nonce.toString())
    }
}