package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class ChainInfo {
    @JsonProperty("server_version")
    var serverVersion: String? = null

    @JsonProperty("chain_id")
    var chainId: String? = null

    @JsonProperty("head_block_num")
    var headBlockNum: String? = null

    @JsonProperty("last_irreversible_block_num")
    var lastIrreversibleBlockNum: Long? = null

    @JsonProperty("last_irreversible_block_id")
    var lastIrreversibleBlockId: String? = null

    @JsonProperty("head_block_id")
    var headBlockId: String? = null

    @JsonProperty("head_block_time")
    var headBlockTime: Date? = null

    @JsonProperty("head_block_producer")
    var headBlockProducer: String? = null

    @JsonProperty("virtual_block_cpu_limit")
    var virtualBlockCpuLimit: String? = null

    @JsonProperty("virtual_block_net_limit")
    var virtualBlockNetLimit: String? = null

    @JsonProperty("block_cpu_limit")
    var blockCpuLimit: String? = null

    @JsonProperty("block_net_limit")
    var blockNetLimit: String? = null
}