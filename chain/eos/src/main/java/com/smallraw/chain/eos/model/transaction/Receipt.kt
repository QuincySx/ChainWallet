package com.smallraw.chain.eos.model.transaction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Receipt {
    @JsonProperty("status")
    var status: String? = null

    @JsonProperty("cpu_usage_us")
    var cpuUsageUs: Long? = null

    @JsonProperty("net_usage_words")
    var netUsageWords: Long? = null
}