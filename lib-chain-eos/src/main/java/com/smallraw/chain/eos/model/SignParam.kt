package com.smallraw.chain.eos.model

import java.util.*

class SignParam {
    /**
     * 最新区块时间
     */
    var headBlockTime: Date? = null

    /**
     * 链ID
     */
    var chainId: String? = null

    /**
     * 不可逆区块
     */
    var lastIrreversibleBlockNum: Long? = null

    /**
     * 上一个区块hash前缀
     */
    var refBlockPrefix: Long? = null

    /**
     * 过期时间
     */
    var exp: Long? = null
}