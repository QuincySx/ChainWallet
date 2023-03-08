package com.smallraw.chain.ethereum.extensions

fun String.stripHexPrefix(): String {
    return if (this.startsWith("0x", true)) {
        this.substring(2)
    } else {
        this
    }
}

fun String.compleHexPrefix(): String {
    return if (this.startsWith("0x", true)) {
        this
    } else {
        "0x$this"
    }
}