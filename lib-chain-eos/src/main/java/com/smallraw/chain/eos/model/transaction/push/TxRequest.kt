package com.smallraw.chain.eos.model.transaction.push

import com.smallraw.chain.eos.model.BaseVo

class TxRequest(var compression: String?, var transaction: Tx?, var signatures: Array<String>) :
    BaseVo()