package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class TableRows {
    var more: Boolean? = null
    var rows: List<Map<*, *>>? = null
}