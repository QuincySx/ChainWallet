package com.smallraw.chain.lib.bitcoin.transaction.build

import android.util.Log
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.bitcoin.transaction.script.Sighash
import com.smallraw.chain.lib.bitcoin.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.lib.crypto.Secp256K1
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.extensions.toHex

class InputSigner(private val privateKeyProvider: PrivateKeyProvider) {
    fun sigScriptData(
        transaction: MutableBTCTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        index: Int
    ): List<ByteArray> {
        val sigHashValue = Sighash.ALL
        val input = inputsToSign[index]
        val prevOutput = input.address

        val privateKey = checkNotNull(privateKeyProvider.findByAddress(input.address)) {
            throw Error.NoPrivateKey()
        }
        val publicKey = Secp256K1.createPublicKey(privateKey.encoded, true)

        val txContent = TransactionSerializer.serializeForSignature(
            transaction,
            inputsToSign,
            outputs,
            index,
            input.isWitness
        ) + byteArrayOf(sigHashValue, 0, 0, 0)

        Log.e("RawTransactionUnitTest", txContent.toHex())
//        ) + byteArrayOf(sigHashValue, 0, 0, 0)
//        val txContent = TransactionSerializer.serializeForSignature(transaction, inputsToSign, outputs, index, input.isWitness) + byteArrayOf(network.sigHashValue, 0, 0, 0)
//        val txContent = TransactionSerializer.serializeForSignature(transaction, inputsToSign, outputs, index, input.isWitness || network.sigHashForked) + byteArrayOf(network.sigHashValue, 0, 0, 0)
        val doubleSha256 = Sha256.doubleSha256(txContent)
        val signature =
            DEREncode.sigToDer(Secp256K1.sign(privateKey.encoded, doubleSha256)) + sigHashValue

        Log.e("SignTransaction", signature.toHex())
        // 3044022044ef433a24c6010a90af14f7739e7c60ce2c5bc3eab96eaee9fbccfdbb3e272202205372a617cb235d0a0ec2889dbfcadf15e10890500d184c8dda90794ecdf7949201
        // 304402207417637a528f1fc28bd003aa0cd36adabf519fffe6df54d3afd7c4ee0eb8769d0220659c3f2ad9b8a82f2498aada1521275730857f3f0072e692218106beffe2c4bb000001
        return when (prevOutput.scriptType) {
            ScriptType.P2PK -> listOf(signature)
            else -> listOf(signature, publicKey)
        }
    }

    open class Error : Exception() {
        class NoPrivateKey : Error()
        class NoPreviousOutput : Error()
        class NoPreviousOutputAddress : Error()
    }
}