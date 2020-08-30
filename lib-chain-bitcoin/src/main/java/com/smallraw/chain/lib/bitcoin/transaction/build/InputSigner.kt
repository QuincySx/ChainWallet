package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.bitcoin.transaction.script.Sighash
import com.smallraw.chain.lib.bitcoin.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import com.smallraw.chain.lib.crypto.Sha256

class InputSigner(private val privateKeyPairProvider: IPrivateKeyPairProvider) {
    fun sigScriptData(
        transaction: MutableBTCTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        index: Int
    ): List<ByteArray> {
        val sigHashValue = Sighash.ALL
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
        val secp256k1Signer = Secp256k1Signer().sign(privateKeyPair.getPrivateKey(), doubleSha256)
        val signature =
            DEREncode.sigToDer(secp256k1Signer.signature()) + sigHashValue
        return when (prevOutput.scriptType) {
            ScriptType.P2PK -> listOf(signature)
            else -> listOf(signature, publicKey.encoded)
        }
    }

    open class Error : Exception() {
        class NoPrivateKey : Error()
        class NoPreviousOutput : Error()
        class NoPreviousOutputAddress : Error()
    }
}