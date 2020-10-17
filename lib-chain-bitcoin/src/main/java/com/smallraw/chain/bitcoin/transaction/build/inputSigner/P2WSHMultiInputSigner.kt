package com.smallraw.chain.bitcoin.transaction.build.inputSigner

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.Signature
import com.smallraw.chain.bitcoincore.script.OP_CHECKMULTISIG
import com.smallraw.chain.bitcoincore.script.OpCodes
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.chain.bitcoincore.script.isOP

class P2WSHMultiInputSigner(
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

        if (input.scriptPubKeyType != ScriptType.P2WSH) {
            throw Error.NotSupportScriptType()
        }

        val chunks = input.redeemScript?.chunks ?: throw Error.NotSupportScriptType()
        // OP_M <PK1> <PK2> <PK3> OP_N CHECKMULTISIG
        if (chunks.size < 4) {
            throw Error.NotSupportScriptType()
        }
        val m = OpCodes.opToIntValue(chunks[0])
        val n = OpCodes.opToIntValue(chunks[chunks.size - 2])
        if (m < 1 || m > 16) {
            throw Error.NotSupportScriptType()
        }
        if (n < 1 || n > 16) {
            throw Error.NotSupportScriptType()
        }
        if (!chunks.last().isOP(OP_CHECKMULTISIG)) {
            throw Error.NotSupportScriptType()
        }

        val signatures = ArrayList<Signature>(m)

        val hashTransaction = hashTransaction(
            transaction,
            inputsToSign,
            outputs,
            signInputIndex,
            input.redeemScript,
            sigHashValue,
            input.isWitness
        )

        for (index in 1..n) {
            if (signatures.size >= m) {
                break
            }
            val privateKeyPair =
                privateKeyPairProvider.findByPublicKey(PublicKey(chunks[index].toBytes()))
                    ?: continue

            val signature = sign(
                privateKeyPair.getPrivateKey(),
                hashTransaction,
                sigHashValue
            )

            signatures.add(signature)
        }

        if (signatures.size == 0) {
            throw Error.NotSupportScriptType()
        }

        val sigData = ArrayList<ByteArray>(m + 2)
        sigData.add(byteArrayOf())
        sigData.addAll(signatures.map { it.signature() })
        sigData.add(input.redeemScript.scriptBytes)

        return ScriptData(sigData)
    }
}