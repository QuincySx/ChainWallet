package com.smallraw.chain.eos.model.transaction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.deser.Deserializers

@JsonIgnoreProperties(ignoreUnknown = true)
class Transaction : Deserializers.Base() {
    @JsonProperty("transaction_id")
    var transactionId: String? = null

    @JsonProperty("processed")
    var processed: Processed? = null
}