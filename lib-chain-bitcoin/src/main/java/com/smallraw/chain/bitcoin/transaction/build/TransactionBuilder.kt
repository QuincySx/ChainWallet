package com.smallraw.chain.bitcoin.transaction.build

import com.smallraw.chain.bitcoin.models.UnspentOutputWithAddress
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IChangeSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IRecipientSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.ITransactionSigner

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
     * 组装交易的参数
     * @param unspentOutputWiths 交易需要使用的 UTXO
     * @param recipientAddress 转账接收地址
     * @param recipientAddress 转账接收金额
     * @param changeAddress 转账找零地址
     * @param changeValue 转账找零金额
     */
    fun build(
        unspentOutputWiths: List<UnspentOutputWithAddress>,
        outputs: List<TransactionOutput> = emptyList(),
        recipientAddress: String? = null,
        recipientValue: Long = 0L,
        changeAddress: String? = null,
        changeValue: Long = 0L,
        version: Int = 2
    ): MutableTransaction {
        val mutableBTCTransaction = MutableTransaction()

        mutableBTCTransaction.version = version

        recipientAddress?.let {
            iRecipientSetter.setRecipient(mutableBTCTransaction, it, recipientValue)
        }
        changeAddress?.let {
            iChangeSetter.setChange(mutableBTCTransaction, it, changeValue)
        }
        inputSetter.setInputs(mutableBTCTransaction, unspentOutputWiths)
        outputSetter.setOutputs(outputs, mutableBTCTransaction)

        btcTransactionSigner.sign(mutableBTCTransaction)
        return mutableBTCTransaction
    }

    open class BuilderException : Exception() {
        class FeeMoreThanValue : BuilderException()
        class NotSupportedScriptType : BuilderException()
    }
}