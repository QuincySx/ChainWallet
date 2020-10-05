package com.smallraw.chain.bitcoin.unspentOutput

import com.smallraw.chain.bitcoin.models.UnspentOutput

data class SelectedUnspentOutputInfo(
    val outputs: List<UnspentOutput>,
    val recipientValue: Long,
    val changeValue: Long? = null
)

class UnspentOutputSelector(unspentOutputProvider: IUnspentOutputProvider) {
    /**
     * @param amount 转账金额
     * @param feeRate 手续费百分比
     * @param receiveType 接收地址类型
     * @param changeType 找零地址类型
     * @param senderPay 手续费在转账金额里扣除，还是另支付手续费
     */
    fun select(
        amount: Long,
        feeRate: Int,
        receiveType: Byte,
        changeType: Byte,
        senderPay: Boolean
    ): SelectedUnspentOutputInfo {
        // todo
        return SelectedUnspentOutputInfo(arrayListOf(), 1000)
    }
}