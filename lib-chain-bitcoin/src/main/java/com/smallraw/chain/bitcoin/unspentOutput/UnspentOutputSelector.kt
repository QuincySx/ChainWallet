package com.smallraw.chain.bitcoin.unspentOutput

import com.smallraw.chain.bitcoin.models.UnspentOutput
import com.smallraw.chain.bitcoin.transaction.TransactionSizeCalculator
import com.smallraw.chain.bitcoin.transaction.build.TransactionOutput
import com.smallraw.chain.bitcoincore.script.ScriptType

data class SelectedUnspentOutputInfo(
    val outputs: List<UnspentOutput>,
    val recipientValue: Long,
    val changeValue: Long? = null
)

sealed class SendValueErrors : Exception() {
    object Dust : SendValueErrors() // 太少不够手续费
    object EmptyOutputs : SendValueErrors()
    object InsufficientUnspentOutputs : SendValueErrors()
    object NoSingleOutput : SendValueErrors()
}

interface IUnspentOutputSelector {
    fun select(
        amount: Long,
        feeRate: Int,
        receiveType: ScriptType,
        changeType: ScriptType,
        senderPay: Boolean,
        dust: Int,
    ): SelectedUnspentOutputInfo
}

class UnspentOutputSelector(
    private val calculator: TransactionSizeCalculator,
    private val unspentOutputProvider: IUnspentOutputProvider,
    private val unspentOutputsLimit: Int? = null //最多用几个 UTXO
) : IUnspentOutputSelector {
    /**
     * @param amount 转账金额
     * @param feeRate 手续费百分比
     * @param receiveType 接收地址类型
     * @param changeType 找零地址类型
     * @param senderPay 手续费在转账金额里扣除，还是另支付手续费
     */
    override fun select(
        amount: Long,
        feeRate: Int,
        receiveType: ScriptType,
        changeType: ScriptType,
        senderPay: Boolean,
        dust: Int,
    ): SelectedUnspentOutputInfo {
        if (amount <= dust) {
            throw SendValueErrors.Dust
        }

        val selectedOutputs = mutableListOf<UnspentOutput>()
        var totalSelectedAmount = 0L
        var feeAmount = 0L
        var recipientAmount = 0L
        var sentAmount = 0L

        while (true) {
            val unspentOutputs = unspentOutputProvider.nextUtxoList()

            if (unspentOutputs.isEmpty()) {
                break
            }

            for (unspentOutput in unspentOutputs) {
                selectedOutputs.add(unspentOutput)
                totalSelectedAmount += unspentOutput.value

                // 删除最小的 value
                unspentOutputsLimit?.let {
                    if (selectedOutputs.size > it) {
                        val outputToExclude = selectedOutputs.sortedBy { it.value }.first()
                        selectedOutputs.removeAt(0)
                        totalSelectedAmount -= outputToExclude.value
                    }
                }

                feeAmount = calculator.transactionSize(
                    selectedOutputs.map {
                        TransactionOutput(
                            it.address,
                            it.value,
                            scriptType = it.scriptType,
                            redeemScript = it.redeemScript
                        )
                    },
                    listOf(receiveType),
                    0
                ) * feeRate

                recipientAmount = if (senderPay) amount else amount - feeAmount
                sentAmount = if (senderPay) amount + feeAmount else amount

                if (sentAmount <= recipientAmount) {      // totalValue is enough
                    if (recipientAmount >= dust) {   // receivedValue won't be dust
                        break
                    } else {
                        // Here senderPay is false, because otherwise "dust" exception would throw far above.
                        // Adding more UTXOs will make fee even greater, making recipientValue even less and dust anyway
                        throw SendValueErrors.Dust
                    }
                }
            }

            if (totalSelectedAmount >= sentAmount) {
                break
            }
        }

        if (selectedOutputs.isEmpty()) {
            throw SendValueErrors.EmptyOutputs
        }

        if (totalSelectedAmount < sentAmount) {
            throw SendValueErrors.InsufficientUnspentOutputs
        }

        val changeOutputHavingTransactionFee = calculator.transactionSize(
            selectedOutputs.map {
                TransactionOutput(
                    it.address,
                    it.value,
                    scriptType = it.scriptType,
                    redeemScript = it.redeemScript
                )
            },
            listOf(receiveType, changeType),
            0
        ) * feeRate

        val withChangeRecipientValue =
            if (senderPay) amount else amount - changeOutputHavingTransactionFee
        val withChangeSentValue =
            if (senderPay) amount + changeOutputHavingTransactionFee else amount
        // if selected UTXOs total value >= recipientValue(toOutput value) + fee(for transaction with change output) + dust(minimum changeOutput value)
        if (totalSelectedAmount >= withChangeRecipientValue + changeOutputHavingTransactionFee + dust) {
            // totalValue is too much, we must have change output
            if (withChangeRecipientValue <= dust) {
                throw SendValueErrors.Dust
            }

            return SelectedUnspentOutputInfo(
                selectedOutputs,
                withChangeRecipientValue,
                totalSelectedAmount - withChangeSentValue
            )
        }
        // No change needed
        return SelectedUnspentOutputInfo(selectedOutputs, recipientAmount, null)
    }
}