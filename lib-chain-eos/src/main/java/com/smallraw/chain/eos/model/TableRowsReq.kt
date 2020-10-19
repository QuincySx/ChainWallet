package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class TableRowsReq {
    var code = "eosio"
    var scope: String? = null
    var table: String? = null
    var json = true
    var limit = 10
}