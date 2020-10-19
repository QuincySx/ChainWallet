package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Transfer(
    /**
     * quantity : 6.0000 EOS
     * memo : 恭喜发财,大吉大利
     * from : lucan222
     * to : eosio.token
     */
    var quantity: String, var memo: String, var from: String, var to: String
)