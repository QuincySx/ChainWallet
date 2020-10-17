package com.smallraw.chain.bitcoin.transaction.build.inputSigner

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_CHECKSIG
import com.smallraw.chain.bitcoincore.script.OP_DUP
import com.smallraw.chain.bitcoincore.script.OP_EQUALVERIFY
import com.smallraw.chain.bitcoincore.script.OP_HASH160
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType

class P2WPKHInputSigner(
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

        if (input.scriptPubKeyType != ScriptType.P2WPKH) {
            throw Error.NotSupportScriptType()
        }

        val privateKeyPair = checkNotNull(privateKeyPairProvider.findByAddress(input.address)) {
            throw Error.NoPrivateKey()
        }
        val publicKey = privateKeyPair.getPublicKey()

        val script = Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk(publicKey.getHash()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )

        val hashTransaction = hashTransaction(
            transaction,
            inputsToSign,
            outputs,
            signInputIndex,
            script,
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