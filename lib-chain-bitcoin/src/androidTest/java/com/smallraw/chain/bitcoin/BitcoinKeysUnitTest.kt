package com.smallraw.chain.bitcoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.bitcoin.crypto.Secp256k1Signer
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.lib.extensions.toHex
import com.smallraw.chain.bitcoin.network.MainNet
import com.smallraw.chain.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.bitcoin.transaction.script.SigHash
import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestPrivateKeys {

    @Test
    fun test_wif_creation() {
        val privateKey =
            WalletImportFormat.decode("KwDiBf89QgGbjEhKnhXJuH7LrciVrZi3qYjgd9M7rFU73sVHnoWn")

        val secp256k1PrivateKey = Secp256k1PrivateKey(privateKey.privateKey)
        val keyBytes =
            "0000000000000000000000000000000000000000000000000000000000000001".hexToByteArray()
        Assert.assertArrayEquals(secp256k1PrivateKey.encoded, keyBytes)

        val wif = WalletImportFormat(
            MainNet(),
            false
        ).format(secp256k1PrivateKey.encoded)
        Assert.assertEquals(wif, "5HpHagT65TZzG1PH3CSu63k8DbpvD8s5ip4nEB3kEsreAnchuDf")
    }

    @Test
    fun test_exponent_creation() {
        val keyBytes =
            "0000000000000000000000000000000000000000000000000000000000000001".hexToByteArray()

        val secp256k1PrivateKey = Secp256k1PrivateKey(keyBytes)

        Assert.assertArrayEquals(secp256k1PrivateKey.encoded, keyBytes)

        val wif = WalletImportFormat(
            MainNet(),
            false
        ).format(secp256k1PrivateKey.encoded)
        Assert.assertEquals(wif, "5HpHagT65TZzG1PH3CSu63k8DbpvD8s5ip4nEB3kEsreAnchuDf")
    }

    @Test
    fun test_public_key() {
        val keyBytes =
            "0000000000000000000000000000000000000000000000000000000000000001".hexToByteArray()

        val secp256k1PrivateKey = Secp256k1PrivateKey(keyBytes)

        val secp256k1KeyPair = Secp256k1KeyPair(secp256k1PrivateKey, compressed = true)

        Assert.assertEquals(
            secp256k1KeyPair.getPublicKey().encoded.toHex(),
            "0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798"
        )
    }
}

@RunWith(AndroidJUnit4::class)
class TestSignAndVerify {
    @Test
    fun test_sign_and_verify() {
        val privateKey =
            WalletImportFormat.decode("KwDiBf89QgGbjEhKnhXJuH7LrciVrZi3qYjgd9M7rFU73sVHnoWn")

        val secp256k1PrivateKey = Secp256k1PrivateKey(privateKey.privateKey)
        val message = "The test!"
        val sign = Secp256k1Signer().sign(secp256k1PrivateKey, message.toByteArray())
        val signature = DEREncode.sigToDer(sign.signature()) + SigHash.ALL

        val publicKey = Secp256k1KeyPair(secp256k1PrivateKey).getPublicKey()

        Assert.assertArrayEquals(
            signature,
            "30440220709a42df49729738446b8c470967fe98e19495b2c5b6b5e17453459df5245d7602204760f4cfa0c15e3b158e86b234f1bd2c33d1026f9050e8df86085fd023c7e3a2000001".hexToByteArray()
        )

        val derToSig = DEREncode.derToSig(signature)
        Log.e("=====", derToSig.toHex())
        Assert.assertEquals(
            Secp256k1Signer().verify(publicKey.encoded, derToSig, message.toByteArray()),
            true
        )
    }
}

@RunWith(AndroidJUnit4::class)
class TestP2pkhAddresses {
    @Test
    fun test_creation_hash() {
        val bitcoinKit = BitcoinKit()
        val p2PKHAddress =
            bitcoinKit.convertAddress(
                "91b24bf9f5288532960ac687abb035127b1d28a5".hexToByteArray(),
                ScriptType.P2PKH
            )
        Assert.assertEquals(p2PKHAddress.address, "1EHNa6Q4Jz2uvNExL497mE43ikXhwF6kZm")

        val p2PKHAddress1 =
            bitcoinKit.convertAddress(
                "751e76e8199196d454941c45d1b3a323f1433bd6".hexToByteArray(),
                ScriptType.P2PKH
            )
        Assert.assertEquals(p2PKHAddress1.address, "1BgGZ9tcN4rm9KBzDn7KprQz87SZ26SAMH")

        val generateKeyPair = bitcoinKit.generateKeyPair()
        Log.e("privateKey",generateKeyPair.getPrivateKey().getKey().toHex())
        Log.e("publicKey",generateKeyPair.getPublicKey().getKey().toHex())
        Log.e("address",bitcoinKit.convertAddress(generateKeyPair.getPublicKey(),ScriptType.P2PKH).address)
    }

    @Test
    fun test_creation_address() {
        val bitcoinKit = BitcoinKit()
        val convertAddress = bitcoinKit.convertAddress("1EHNa6Q4Jz2uvNExL497mE43ikXhwF6kZm")
        Assert.assertArrayEquals(
            convertAddress.hashKey,
            "91b24bf9f5288532960ac687abb035127b1d28a5".hexToByteArray()
        )

        val convertAddress1 = bitcoinKit.convertAddress("1BgGZ9tcN4rm9KBzDn7KprQz87SZ26SAMH")
        Assert.assertArrayEquals(
            convertAddress1.hashKey,
            "751e76e8199196d454941c45d1b3a323f1433bd6".hexToByteArray()
        )
    }
}