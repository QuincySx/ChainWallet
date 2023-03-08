package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Block {
    @JsonProperty("timestamp")
    var timestamp: Date? = null

    @JsonProperty("producer")
    var producer: String? = null

    @JsonProperty("confirmed")
    var confirmed: Long? = null

    @JsonProperty("previous")
    var previous: String? = null

    @JsonProperty("transaction_mroot")
    var transactionMroot: String? = null

    @JsonProperty("action_mroot")
    var actionMroot: String? = null

    @JsonProperty("schedule_version")
    var scheduleVersion: String? = null

    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("block_num")
    var blockNum: Long? = null

    @JsonProperty("ref_block_prefix")
    var refBlockPrefix: Long? = null
}