package com.smallraw.chain.bitcoin.transaction.build.inputSigner

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.transaction.serializer.MutableTransactionSerializer
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.Signature
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.crypto.core.crypto.Sha256

open class Error : Exception() {
    class NotSupportScriptType : Error()
    class NoPrivateKey : Error()
    class NoPreviousOutput : Error()
    class NoPreviousOutputAddress : Error()
}

data class ScriptData(
    val data: List<ByteArray>
)

abstract class InputSignerAbstract(
    protected val privateKeyPairProvider: IPrivateKeyPairProvider
) {
    abstract fun sigScriptData(
        transaction: MutableTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        signInputIndex: Int,
        sigHashValue: Byte = SigHash.ALL
    ): ScriptData

    protected fun hashTransaction(
        transaction: MutableTransaction, inputsToSigns: List<InputToSign>,
        outputs: List<TransactionOutput>,
        index: Int,
        script: Script,
        sigHash: Byte = SigHash.ALL,
        isWitness: Boolean
    ): ByteArray {
        val txContent = MutableTransactionSerializer.serializeForSignature(
            transaction,
            inputsToSigns,
            outputs,
            index,
            script,
            isWitness,
            sigHash
        )
        return Sha256.doubleSha256(txContent)
    }

    protected fun sign(
        privateKey: PrivateKey,
        transactionHash: ByteArray,
        sigHashValue: Byte = SigHash.ALL,
    ): Signature {
        return privateKey.sign(transactionHash, sigHashValue)
    }
}