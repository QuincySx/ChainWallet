package com.smallraw.chain.eos.model.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class NetLimit {
    var used: Long? = null
    var available: Long? = null
    var max: Long? = null
}