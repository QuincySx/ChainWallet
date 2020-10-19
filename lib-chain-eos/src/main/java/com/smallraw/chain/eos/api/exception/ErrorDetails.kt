package com.smallraw.chain.eos.api.exception

import com.fasterxml.jackson.annotation.JsonProperty

class ErrorDetails private constructor() {
    var message: String? = null
    var file: String? = null

    @set:JsonProperty("line_number")
    var lineNumber: Int? = null
    var method: String? = null
}