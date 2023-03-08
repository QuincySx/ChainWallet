package com.smallraw.chain.eos.model.transaction.push

import com.smallraw.chain.eos.model.BaseVo
import java.util.*

class TxAction(val actor: String?, val account: String?, val name: String?, val data: Any?) :
    BaseVo() {
    private var authorization: MutableList<TxActionAuth> = ArrayList()

    init {
        authorization.add(TxActionAuth(actor, "active"))
    }

    fun getAuthorization(): List<TxActionAuth>? {
        return authorization
    }

    fun setAuthorization(authorization: MutableList<TxActionAuth>) {
        this.authorization = authorization
    }
}