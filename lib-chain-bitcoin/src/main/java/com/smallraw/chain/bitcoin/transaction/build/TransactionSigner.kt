package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.transaction.build.`interface`.ITransactionSigner
import com.smallraw.chain.bitcoin.transaction.build.inputSigner.InputSignerChain
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptChunk
import com.smallraw.chain.bitcoincore.script.ScriptType

class TransactionSigner(private val inputSigner: InputSignerChain) : ITransactionSigner {
    override fun sign(mutableTransaction: MutableTransaction) {
        val inputsToSign = mutableTransaction.inputsToSign
        val outputs = mutableTransaction.outputs

        inputsToSign.forEachIndexed { index, inputToSign ->
            val sigData =
                inputSigner.sigScriptData(mutableTransaction, inputsToSign, outputs, index)

            when (inputToSign.scriptPubKeyType) {
                ScriptType.P2PK,
                ScriptType.P2SH,
                ScriptType.P2PKH -> {
                    val chunks = ArrayList<ScriptChunk>(sigData.sigScript.size)
                    sigData.sigScript.forEach {
                        chunks.add(Chunk(it))
                    }
                    inputToSign.sigScript = Script(chunks).scriptBytes
                }
                ScriptType.P2WSH,
                ScriptType.P2WPKH -> {
                    mutableTransaction.segwit = true
                    inputToSign.witness = sigData.witness
                }
                else -> throw TransactionBuilder.BuilderException.NotSupportedScriptType()
            }
        }
    }
}

class NoRedeemScriptException : Exception()