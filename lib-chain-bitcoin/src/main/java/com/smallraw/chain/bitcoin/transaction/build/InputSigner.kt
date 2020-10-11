package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.transaction.serializer.MutableTransactionSerializer
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.Signature
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.chain.lib.core.crypto.Sha256

class InputSigner(
    private val privateKeyPairProvider: IPrivateKeyPairProvider,
    private val sigHashValue: Byte = SigHash.ALL
) {
    data class ScriptData(
        val signatures: Signature,
        val publicKey: PublicKey,
    )

    fun sigScriptData(
        transaction: MutableTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        index: Int
    ): ScriptData {
        val input = inputsToSign[index]


        val privateKeyPair = checkNotNull(privateKeyPairProvider.findByAddress(input.address)) {
            throw Error.NoPrivateKey()
        }
        val publicKey = privateKeyPair.getPublicKey()

        val txContent = MutableTransactionSerializer.serializeForSignature(
            transaction,
            inputsToSign,
            outputs,
            index,
            input.isWitness
        ) + byteArrayOf(sigHashValue, 0, 0, 0)// 相当写入了一个 Int32

        val doubleSha256 = Sha256.doubleSha256(txContent)

        val signature = privateKeyPair.getPrivateKey().sign(doubleSha256, sigHashValue)

        return ScriptData(signature, publicKey)
    }

    open class Error : Exception() {
        class NoPrivateKey : Error()
        class NoPreviousOutput : Error()
        class NoPreviousOutputAddress : Error()
    }
}