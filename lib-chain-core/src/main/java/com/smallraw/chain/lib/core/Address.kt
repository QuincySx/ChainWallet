package com.smallraw.chain.lib.core

interface Address {
    fun getFormat(): String
    fun getAddress(): ByteArray
}