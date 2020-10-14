package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoincore.Signature
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.chain.bitcoincore.transaction.Transaction
import com.smallraw.crypto.core.extensions.hexToByteArray

/**
 * 存放 input。output 等基本信息。
 */
class MutableTransaction {
    var version: Int = 2
    var lockTime: Int = 0
    val inputsToSign = mutableListOf<InputToSign>()
    var outputs = listOf<TransactionOutput>()

    var segwit: Boolean = false
    val witnesses: Array<Signature> = arrayOf()

    //region 如果懂的比特币可以直接拼装 outputs
    // 收款地址
    var recipientAddress: Address? = null

    // 收款金额
    var recipientValue = 0L

    // 找零地址
    var changeAddress: Address? = null

    // 找零金额
    var changeValue = 0L
    //endregion

    fun build(): Transaction {
        val redemptionScripts = ArrayList<Script>(inputsToSign.size)
        val inputs: Array<Transaction.Input> = inputsToSign.map { input ->
            input.redeemScript?.let { redemptionScripts.add(it) }
            Transaction.Input(
                input.txId.hexToByteArray(),
                input.index,
                Script(input.sigScript),
                input.sequence
            )
        }.toTypedArray()
        val outputs: Array<Transaction.Output> = outputs.map { output ->
            Transaction.Output(
                output.value,
                output.address.scriptPubKey()
            )
        }.toTypedArray()

        // todo witnesses
        return Transaction(inputs, outputs, version, lockTime)
    }
}

data class InputToSign(
    // utxo 所属地址
    val address: Address,// hash of address
    // utxo txid
    val txId: String,
    // utxo output index
    val index: Int,
    // utxo 锁定时间
    val sequence: Int = 0xffffffff.toInt(),
    // utxo 赎回脚本
    val redeemScript: Script? = null,
    // utxo 是否是隔离见证
    var isWitness: Boolean = false,
    // input 锁定脚本的类型
    var scriptPubKeyType: ScriptType = ScriptType.P2PKH
) {
    // input 签名
    var sigScript: ByteArray = byteArrayOf()

    // input 隔离见证签名
    var witness: List<ByteArray> = arrayListOf()
}

data class TransactionOutput(
    // output 转账的地址
    var address: Address,
    // output 输出金额
    var value: Long = 0,
    // output 输出索引
    var index: Int = 0,
    // output 赎回脚本：ScriptInput
    var redeemScript: ByteArray? = null,
    var scriptType: ScriptType = ScriptType.UNKNOWN,
    // P2SH 签字脚本
    var signatureScriptFunction: ((List<ByteArray>) -> ByteArray)? = null
)