package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.transaction.BTCTransaction
import com.smallraw.chain.lib.bitcoin.transaction.script.Script
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import java.security.Signature

/**
 * 存放 input。output 等基本信息。
 */
class MutableBTCTransaction {
    var version: Int = 2
    var lockTime: Int = 0
    val inputsToSign = mutableListOf<InputToSign>()
    var outputs = listOf<TransactionOutput>()

    val segwit: Boolean = false
//    val witnesses: Array<Signature> = arrayOf()

    lateinit var recipientAddress: BitcoinAddress
    var recipientValue = 0L

    var changeAddress: BitcoinAddress? = null
    var changeValue = 0L

    fun build(): BTCTransaction {
        val redemptionScripts = ArrayList<Script>(inputsToSign.size)
        val inputs: Array<BTCTransaction.Input> = inputsToSign.map { input ->
            input.redemptionScript?.let { redemptionScripts.add(it) }
            BTCTransaction.Input(
                input.txId.hexStringToByteArray(),
                input.index,
                input.lockScript,
                input.sequence
            )
        }.toTypedArray()
        val outputs: Array<BTCTransaction.Output> = outputs.map { output ->
            BTCTransaction.Output(
                output.value,
                Script(output.lockingScript)
            )
        }.toTypedArray()

        // todo witnesses
        return BTCTransaction(version, lockTime, inputs, outputs, segwit)
    }
}

data class InputToSign(
    val txId: String,
    val index: Int,
    val sequence: Int = 0xffffffff.toInt(),
    val lockScript: Script? = null,
    val redemptionScript: Script? = null
)

data class TransactionOutput(
    var value: Long = 0,
    var index: Int = 0,
    var lockingScript: ByteArray = byteArrayOf(),
    var scriptType: ScriptType = ScriptType.UNKNOWN,
    var keyHash: ByteArray? = null,
    var address: String? = null
) {
    var redeemScript: ByteArray? = null
}