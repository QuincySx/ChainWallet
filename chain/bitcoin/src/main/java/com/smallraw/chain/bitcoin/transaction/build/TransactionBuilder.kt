package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.models.UnspentOutput
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IChangeSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IRecipientSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.ITransactionSigner
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.transaction.Transaction

/**
 * 比特币交易组装器
 * @param iRecipientSetter 收款地址处理器
 * @param iChangeSetter 找零地址处理器
 * @param inputSetter UTXO 输入处理器
 * @param outputSetter 交易输出处理器
 * @param btcTransactionSigner 交易签名处理器
 */
class TransactionBuilder(
    private val iRecipientSetter: IRecipientSetter,
    private val iChangeSetter: IChangeSetter,
    private val inputSetter: InputSetter,
    private val outputSetter: OutputSetter,
    private val btcTransactionSigner: ITransactionSigner = EmptyTransactionSigner()
) {

    /**
     * 组装交易的参数,手动选择 UTXO，自行指定手续费。
     * @param unspentOutputWiths 交易需要使用的 UTXO
     * @param recipientAddress 转账接收地址
     * @param recipientAddress 转账接收金额
     * @param changeAddress 转账找零地址
     * @param changeValue 转账找零金额
     * @param otherOutput OP_RETURN 等特殊输出
     */
    fun build(
        unspentOutputWiths: List<UnspentOutput>? = null,
        recipientAddress: String? = null,
        recipientValue: Long = 0L,
        changeAddress: String? = null,
        changeValue: Long = 0L,
        otherOutput: List<Script>? = null,
        version: Int = 2
    ): Transaction {
        val mutableBTCTransaction = MutableTransaction()

        mutableBTCTransaction.version = version

        recipientAddress?.let {
            iRecipientSetter.setRecipient(mutableBTCTransaction, it, recipientValue)
        }
        changeAddress?.let {
            iChangeSetter.setChange(mutableBTCTransaction, it, changeValue)
        }
        outputSetter.setOutputs(mutableBTCTransaction, otherOutput)
        inputSetter.setInputs(mutableBTCTransaction, unspentOutputWiths)

        btcTransactionSigner.sign(mutableBTCTransaction)
        return mutableBTCTransaction.build()
    }

    /**
     * 组装交易的参数,会自动选择 UTXO，自动计算手续费。
     * @param unspentOutputWiths 交易需要使用的 UTXO
     * @param recipientAddress 转账接收地址
     * @param recipientAddress 转账接收金额
     * @param changeAddress 转账找零地址
     * @param changeValue 转账找零金额
     * @param otherOutput OP_RETURN 等特殊输出
     */
    fun build(
        recipientAddress: String? = null,
        recipientValue: Long = 0L,
        changeAddress: String? = null,
        feeRate: Int,
        senderPay: Boolean,
        otherOutput: List<Script>? = null
    ): Transaction {
        val mutableBTCTransaction = MutableTransaction()

        recipientAddress?.let {
            iRecipientSetter.setRecipient(mutableBTCTransaction, it, recipientValue)
        }
        changeAddress?.let {
            iChangeSetter.setChange(mutableBTCTransaction, it, 0)
        }

        // 如果有 OP_RETURN 等数据，交易大小另算。
        var pluginDataSize = 0
        otherOutput?.forEach {
            pluginDataSize += it.scriptBytes.size
        }

        // 选择 UTXO，计算手续费。
        inputSetter.setInputs(mutableBTCTransaction, feeRate, senderPay)

        outputSetter.setOutputs(mutableBTCTransaction, otherOutput)

        btcTransactionSigner.sign(mutableBTCTransaction)
        return mutableBTCTransaction.build()
    }

    open class BuilderException : Exception() {
        class FeeMoreThanValue : BuilderException()
        class NotSupportedScriptType : BuilderException()
    }
}