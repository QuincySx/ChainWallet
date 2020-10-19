package com.smallraw.chain.eos.api.exception

class Error private constructor() {
    var code: String? = null
    var name: String? = null
    var what: String? = null
    var details: Array<ErrorDetails>? = null
}