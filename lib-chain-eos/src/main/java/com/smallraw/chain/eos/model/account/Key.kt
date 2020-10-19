package com.smallraw.chain.eos.model.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Key {
    var key: String? = null
    var weight: Long? = null
}