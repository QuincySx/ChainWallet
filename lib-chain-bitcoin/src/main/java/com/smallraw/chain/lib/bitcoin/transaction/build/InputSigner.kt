package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.bitcoin.transaction.script.Sighash
import com.smallraw.chain.lib.bitcoin.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.lib.crypto.Secp256K1
import com.smallraw.chain.lib.crypto.Sha256

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
//        val txContent = TransactionSerializer.serializeForSignature(transaction, inputsToSign, outputs, index, input.isWitness) + byteArrayOf(network.sigHashValue, 0, 0, 0)
//        val txContent = TransactionSerializer.serializeForSignature(transaction, inputsToSign, outputs, index, input.isWitness || network.sigHashForked) + byteArrayOf(network.sigHashValue, 0, 0, 0)
        val doubleSha256 = Sha256.doubleSha256(txContent)
        val signature =
            DEREncode.sigToDer(Secp256K1.sign(privateKey.encoded, doubleSha256)) + sigHashValue
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