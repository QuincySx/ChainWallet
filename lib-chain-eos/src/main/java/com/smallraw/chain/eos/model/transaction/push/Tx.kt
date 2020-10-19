package com.smallraw.chain.eos.model.transaction.push

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.smallraw.chain.eos.model.BaseVo
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Tx : BaseVo() {
    var expiration: Any? = null
    var ref_block_num: Long? = null
    var ref_block_prefix: Long? = null
    var net_usage_words: Long? = null
    var max_cpu_usage_ms: Long? = null
    var delay_sec: Long? = null
    var context_free_actions: List<String> = ArrayList()
    var actions: List<TxAction>? = null
    var transaction_extensions: List<TxExtenstions> = ArrayList()
}