package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.bitcoin.transaction.script.SigHash
import com.smallraw.chain.bitcoin.transaction.serializers.TransactionSerializer

class InputSigner(private val privateKeyPairProvider: IPrivateKeyPairProvider) {
    fun sigScriptData(
        transaction: MutableBTCTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        index: Int
    ): List<ByteArray> {
        val sigHashValue = SigHash.ALL
        val input = inputsToSign[index]
        val prevOutput = input.address

        val privateKeyPair = checkNotNull(privateKeyPairProvider.findByAddress(input.address)) {
            throw Error.NoPrivateKey()
        }
        val publicKey = privateKeyPair.getPublicKey()

        val txContent = TransactionSerializer.serializeForSignature(
            transaction,
            inputsToSign,
            outputs,
            index,
            input.isWitness
        ) + byteArrayOf(sigHashValue, 0, 0, 0)// 相当写入了一个 Int32

        val doubleSha256 = Sha256.doubleSha256(txContent)

        val signature = privateKeyPair.getPrivateKey().sign(doubleSha256, sigHashValue).signature()
        return when (prevOutput.scriptType) {
            ScriptType.P2PK -> listOf(signature)
            else -> listOf(signature, publicKey.getKey())
        }
    }

    open class Error : Exception() {
        class NoPrivateKey : Error()
        class NoPreviousOutput : Error()
        class NoPreviousOutputAddress : Error()
    }
}