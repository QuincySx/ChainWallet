package com.smallraw.chain.eos.model.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RequiredAuth {
    var accounts: List<Map<*, *>>? = null
    var keys: List<Key>? = null
    var threshold: Long? = null
}