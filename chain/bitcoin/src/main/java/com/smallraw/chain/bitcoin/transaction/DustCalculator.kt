package com.smallraw.chain.bitcoin.transaction

import com.smallraw.chain.bitcoincore.script.ScriptType

class DustCalculator(dustRelayTxFee: Int, val sizeCalculator: TransactionSizeCalculator) {
    val minFeeRate = dustRelayTxFee / 1000

    fun dust(type: ScriptType): Int {
        // https://github.com/bitcoin/bitcoin/blob/c536dfbcb00fb15963bf5d507b7017c241718bf6/src/policy/policy.cpp#L18

        var size = sizeCalculator.outputSize(type)

        size += if (type.isWitness) {
            sizeCalculator.inputSize(ScriptType.P2WPKH) + sizeCalculator.witnessSize(ScriptType.P2WPKH) / 4
        } else {
            sizeCalculator.inputSize(ScriptType.P2PKH)
        }

        return size * minFeeRate
    }
}