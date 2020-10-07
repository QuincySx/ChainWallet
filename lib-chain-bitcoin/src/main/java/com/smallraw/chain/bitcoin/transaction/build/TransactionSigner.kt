package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.transaction.build.`interface`.ITransactionSigner
import com.smallraw.chain.bitcoin.transaction.script.*

class TransactionSigner(private val inputSigner: InputSigner) : ITransactionSigner {
    override fun sign(mutableTransaction: MutableTransaction) {
        val inputsToSign = mutableTransaction.inputsToSign
        val outputs = mutableTransaction.outputs

        inputsToSign.forEachIndexed { index, inputToSign ->
            val previousOutput = inputToSign.address
            val (signature, publicKey) =
                inputSigner.sigScriptData(mutableTransaction, inputsToSign, outputs, index)

            when (previousOutput.scriptType) {
                ScriptType.P2PK -> {
                    inputToSign.sigScript =
                        ScriptInputP2PK(signature.signature()).scriptBytes
                }
                ScriptType.P2PKH -> {
                    inputToSign.sigScript =
                        ScriptInputP2PKH(signature.signature(), publicKey.getKey()).scriptBytes
                }

                ScriptType.P2WPKH -> {
                    mutableTransaction.segwit = true
                    TODO("")
//                    inputToSign.witness = ScriptInputP2WPKH()
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
                    // TODO: 10/6/20 非标准 P2SH 签字
//                    val signatureScriptFunction = previousOutput.signatureScriptFunction
//                    if (signatureScriptFunction != null) {
//                        // non-standard P2SH signature script
//                        inputToSign.sigScript = signatureScriptFunction(sigScriptData)
//                    } else {
                    // standard (signature, publicKey, redeemScript) signature script
                    inputToSign.sigScript = ScriptInputP2SHMultisig(
                        signature as Bitcoin.MultiSignature,
                        redeemScript
                    ).scriptBytes
//                    }
                }

                else -> throw TransactionBuilder.BuilderException.NotSupportedScriptType()
            }
        }
    }
}

class NoRedeemScriptException : Exception()