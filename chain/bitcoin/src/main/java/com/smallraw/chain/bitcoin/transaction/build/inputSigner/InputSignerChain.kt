package com.smallraw.chain.bitcoin.transaction.build.inputSigner

import com.smallraw.chain.bitcoin.transaction.build.InputToSign
import com.smallraw.chain.bitcoin.transaction.build.MutableTransaction
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoincore.execptions.BitcoinException

class InputSignerChain(
    privateKeyPairProvider: IPrivateKeyPairProvider
) : InputSignerAbstract(privateKeyPairProvider) {

    companion object {
        fun default(privateKeyPairProvider: IPrivateKeyPairProvider): InputSignerChain {
            val inputSignerChain = InputSignerChain(privateKeyPairProvider)
            inputSignerChain.addInputSigner(P2PKHInputSigner(privateKeyPairProvider))
            inputSignerChain.addInputSigner(P2SHMultiInputSigner(privateKeyPairProvider))
            inputSignerChain.addInputSigner(P2WPKHInputSigner(privateKeyPairProvider))
            inputSignerChain.addInputSigner(P2WSHMultiInputSigner(privateKeyPairProvider))
            return inputSignerChain
        }
    }

    private val mInputSigners = mutableListOf<InputSignerAbstract>()

    fun addInputSigner(inputSigner: InputSignerAbstract) {
        mInputSigners.add(inputSigner)
    }

    override fun sigScriptData(
        transaction: MutableTransaction,
        inputsToSign: MutableList<InputToSign>,
        outputs: List<TransactionOutput>,
        signInputIndex: Int,
        sigHashValue: Byte
    ): ScriptData {
        val exceptions = mutableListOf<Exception>()

        for (converter in mInputSigners) {
            try {
                return converter.sigScriptData(
                    transaction,
                    inputsToSign,
                    outputs,
                    signInputIndex,
                    sigHashValue
                )
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }
        val exception = BitcoinException(
            BitcoinException.ERR_INPUT_SIGN,
            "No signer in chain could process the signature"
        ).also {
            exceptions.forEach { it.addSuppressed(it) }
        }
        throw exception
    }
}