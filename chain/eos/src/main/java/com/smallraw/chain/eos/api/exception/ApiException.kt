package com.smallraw.chain.eos.api.exception

class ApiException : RuntimeException {
    var error: ApiError? = null

    constructor(apiError: ApiError?) {
        error = apiError
    }

    constructor(cause: Throwable?) : super(cause) {}

    override val message: String
        get() = if (error != null) {
            error!!.message!!
        } else super.message!!

    companion object {
        private const val serialVersionUID = 1L
    }
}