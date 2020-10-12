package com.smallraw.chain.bitcoincore.execptions

import com.smallraw.crypto.core.extensions.toHex

class ScriptParsingException : java.lang.Exception {
    constructor(script: ByteArray?) : super("Unable to parse script: " + script?.toHex()) {}
    constructor(message: String?) : super(message) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}