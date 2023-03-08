package com.smallraw.chain.eos.model.transaction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Processed {
    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("receipt")
    var receipt: Receipt? = null

    @JsonProperty("elapsed")
    var elapsed: Long? = null

    @JsonProperty("net_usage")
    var netUsage: Long? = null

    @JsonProperty("scheduled")
    var scheduled: Boolean? = null
}