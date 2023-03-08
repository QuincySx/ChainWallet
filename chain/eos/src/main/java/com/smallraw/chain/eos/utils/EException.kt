package com.smallraw.chain.eos.utils

class EException(var code: String, var msg: String) : RuntimeException() {

    companion object {
        private const val serialVersionUID = 1L
    }
}