package com.smallraw.chain.bitcoin.transaction.build.inputSigner

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoincore.script.ScriptType

class P2PKHInputSigner(
    privateKeyPairProvider: IPrivateKeyPairProvider,
) : InputSignerAbstract(privateKeyPairProvider) {
    override fun sigScriptData(
        transaction: MutableTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        signInputIndex: Int,
        sigHashValue: Byte
    ): ScriptData {
        val input = inputsToSign[signInputIndex]

        if (input.scriptPubKeyType != ScriptType.P2PKH) {
            throw Error.NotSupportScriptType()
        }

        val privateKeyPair = checkNotNull(privateKeyPairProvider.findByAddress(input.address)) {
            throw Error.NoPrivateKey()
        }
        val publicKey = privateKeyPair.getPublicKey()

        val hashTransaction = hashTransaction(
            transaction,
            inputsToSign,
            outputs,
            signInputIndex,
            input.address.scriptPubKey(),
            sigHashValue,
            input.isWitness
        )
        val signature = sign(
            privateKeyPair.getPrivateKey(),
            hashTransaction,
            sigHashValue
        )

        return ScriptData(listOf(signature.signature(), publicKey.getKey()))
    }
}