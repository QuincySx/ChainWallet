package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.transaction.script.OpCodes
import com.smallraw.chain.bitcoin.transaction.script.ScriptType

class BTCTransactionSigner(private val inputSigner: InputSigner) : IBTCTransactionSigner {
    override fun sign(mutableBTCTransaction: MutableBTCTransaction) {
        val inputsToSign = mutableBTCTransaction.inputsToSign
        val outputs = mutableBTCTransaction.outputs

        inputsToSign.forEachIndexed { index, inputToSign ->
            val previousOutput = inputToSign.address
//            val publicKey = inputToSign.previousOutputPublicKey
            val sigScriptData =
                inputSigner.sigScriptData(mutableBTCTransaction, inputsToSign, outputs, index)

            when (previousOutput.scriptType) {
                ScriptType.P2PKH -> {
                    inputToSign.sigScript = signatureScript(sigScriptData)
                }

                ScriptType.P2WPKH -> {
                    mutableBTCTransaction.segwit = true
                    inputToSign.witness = sigScriptData
                }

//                ScriptType.P2WPKHSH -> {
//                    mutableTransaction.segwit = true
//                    val witnessProgram = OpCodes.scriptWPKH(publicKey.publicKeyHash)
//
//                    inputToSign.sigScript = signatureScript(listOf(witnessProgram))
//                    inputToSign.witness = sigScriptData
//                }

                ScriptType.P2SH -> {
                    val redeemScript =
                        inputToSign.redeemScript?.scriptBytes ?: throw NoRedeemScriptException()
//                    val signatureScriptFunction = previousOutput.signatureScriptFunction

//                    if (signatureScriptFunction != null) {
//                        // non-standard P2SH signature script
//                        inputToSign.sigScript = signatureScriptFunction(sigScriptData)
//                    } else {
                    // standard (signature, publicKey, redeemScript) signature script
                    inputToSign.sigScript = signatureScript(sigScriptData + redeemScript)
//                    }
                }

                else -> throw BTCTransactionBuilder.BuilderException.NotSupportedScriptType()
            }
        }
    }

    private fun signatureScript(params: List<ByteArray>): ByteArray {
        // 挨个对 List 的元素进行 OpCodes push 操作
        return params.fold(byteArrayOf()) { acc, bytes -> acc + OpCodes.push(bytes) }
    }
}

class NoRedeemScriptException : Exception()