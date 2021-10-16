package com.smallraw.chain.ethereum

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.ethereum.extensions.toHexPrefix
import com.smallraw.chain.ethereum.network.Ropsten
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

        val transaction = Transaction.createTransaction(
            nonce = 0,
            gasPrice = 5000000000,
            gasLimit = 21000,
            to = address,
            value = BigInteger.valueOf(1000000000000000)
        )

        val sha256 = TransactionSerializer.hashForSignature(transaction, Ropsten())
        Log.e("transaction sha256 unit test: ", sha256.toHexPrefix())
        val sign = privateKey.sign(sha256)
        transaction.signature = sign

        Log.e(
            "transaction unit test: ",
            TransactionSerializer.serialize(transaction, Ropsten()).toHexPrefix()
        )
        Assert.assertEquals(
            TransactionSerializer.serialize(transaction, Ropsten()).toHexPrefix(),
            "0xf86b8085012a05f200825208944626f9ea04267a7ff2904f9e6808bd7042cff85887038d7ea4c68000802aa0ead425dc88130affffeeb051a1044a12db642d81f70aca9cc4be1c95a507b5f9a04713feec33ea52618b4562d240a629f6d3dc56dfc02876e9e1c34a3ed8e01d63"
        )

        val deserialize =
            TransactionSerializer.deserialize("0xf86b8085012a05f200825208944626f9ea04267a7ff2904f9e6808bd7042cff85887038d7ea4c68000802aa0ead425dc88130affffeeb051a1044a12db642d81f70aca9cc4be1c95a507b5f9a04713feec33ea52618b4562d240a629f6d3dc56dfc02876e9e1c34a3ed8e01d63".hexToByteArray())
        Assert.assertNotNull(deserialize)
        Log.e("transaction unit test: ", deserialize?.nonce.toString())
    }

    @Test
    fun test_eip1159_transaction() {
        val privateKey =
            PrivateKey.ofHex("89afc24b157548633b6e54e4e7e6f00096cfb0e750854914472fc6571306e849")
        val publicKey = privateKey.getPublicKey()
        val address = publicKey.getAddress()

        val transaction = Transaction.createTransaction(
            nonce = 1,
            maxPriorityFeePerGas = 5000000000.toBigInteger(),
            maxFeePerGas = 5000000000.toBigInteger(),
            gasLimit = 21000,
            to = address,
            value = BigInteger.valueOf(1000000000000000)
        )

        val sha256 = TransactionSerializer.hashForSignature(transaction, Ropsten())
        Log.e("transaction sha256 unit test: ", sha256.toHexPrefix())
        val sign = privateKey.sign(sha256)
        transaction.signature = sign

        Log.e(
            "transaction unit test: ",
            TransactionSerializer.serialize(transaction, Ropsten()).toHexPrefix()
        )
        Assert.assertEquals(
            TransactionSerializer.serialize(transaction, Ropsten()).toHexPrefix(),
            "0x02f873030185012a05f20085012a05f200825208944626f9ea04267a7ff2904f9e6808bd7042cff85887038d7ea4c6800080c080a0e10f704e184ea44aef82cadfc7fa018f9b984a6dddc319872fa800ac8c640c82a0035ae6238363807d466745eab7d6936760a1145d42350abc98bd5fdcceabaca9"
        )

        val deserialize =
            TransactionSerializer.deserialize("0x02f873030185012a05f20085012a05f200825208944626f9ea04267a7ff2904f9e6808bd7042cff85887038d7ea4c6800080c080a0e10f704e184ea44aef82cadfc7fa018f9b984a6dddc319872fa800ac8c640c82a0035ae6238363807d466745eab7d6936760a1145d42350abc98bd5fdcceabaca9".hexToByteArray())

        Assert.assertNotNull(deserialize)
        Log.e("transaction unit test: ", deserialize?.nonce.toString())
    }
}