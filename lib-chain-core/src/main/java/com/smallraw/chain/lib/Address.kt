package com.smallraw.chain.lib

interface Address {
    fun getFormat(): String
    fun getAddress(): ByteArray
}