package com.smallraw.chain.lib.bitcoin.transaction.build

import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.transaction.BTCTransaction
import com.smallraw.chain.lib.bitcoin.transaction.script.Script
import com.smallraw.chain.lib.bitcoin.Bitcoin
import com.smallraw.chain.lib.extensions.hexStringToByteArray

/**
 * 存放 input。output 等基本信息。
 */
class MutableBTCTransaction {
    var version: Int = 2
    var lockTime: Int = 0
    val inputsToSign = mutableListOf<InputToSign>()
    var outputs = listOf<TransactionOutput>()

    var segwit: Boolean = false
//    val witnesses: Array<Signature> = arrayOf()

    lateinit var recipientAddress: Bitcoin.Address
    var recipientValue = 0L

    var changeAddress: Bitcoin.Address? = null
    var changeValue = 0L

    fun build(): BTCTransaction {
        val redemptionScripts = ArrayList<Script>(inputsToSign.size)
        val inputs: Array<BTCTransaction.Input> = inputsToSign.map { input ->
            input.redeemScript?.let { redemptionScripts.add(it) }
            BTCTransaction.Input(
                input.txId.hexStringToByteArray(),
                input.index,
                Script(input.sigScript),
                input.sequence
            )
        }.toTypedArray()
        val outputs: Array<BTCTransaction.Output> = outputs.map { output ->
            BTCTransaction.Output(
                output.value,
                Script(output.address.lockingScript)
            )
        }.toTypedArray()

        // todo witnesses
        return BTCTransaction(version, lockTime, inputs, outputs, segwit)
    }
}

data class InputToSign(
    // utxo 所属地址
    val address: Bitcoin.Address,// hash of address
    // utxo txid
    val txId: String,
    // utxo output index
    val index: Int,
    // utxo 锁定时间
    val sequence: Int = 0xffffffff.toInt(),
    // utxo 锁定脚本
    val lockScript: Script? = null,
    // utxo 赎回脚本
    val redeemScript: Script? = null
) {
    // utxo 所属公钥
    var publicKey: ByteArray = byteArrayOf()

    // utxo 是否是隔离见证
    var isWitness: Boolean = false

    // input 签名
    var sigScript: ByteArray = byteArrayOf()

    // input 隔离见证签名
    var witness: List<ByteArray> = arrayListOf()
}

data class TransactionOutput(
    // output 转账的地址
    var address: Bitcoin.Address,
    // output 输出金额
    var value: Long = 0,
    // output 输出索引
    var index: Int = 0
)
//{
//    // output 赎回脚本
//    var redeemScript: ByteArray? = null
//}