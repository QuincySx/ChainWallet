package com.smallraw.chain.eos.model.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Permission {
    @JsonProperty("perm_name")
    var permName: String? = null

    @JsonProperty("parent")
    var parent: String? = null

    @JsonProperty("required_auth")
    var requiredAuth: RequiredAuth? = null
}