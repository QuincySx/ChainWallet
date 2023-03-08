package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class JsonToBin {
    var binargs: String? = null
        get() = if (field == null) "" else field
    var required_scope: List<String>? = null
        get() = if (field == null) {
            ArrayList()
        } else field
    var required_auth: List<String>? = null
        get() = if (field == null) {
            ArrayList()
        } else field
}