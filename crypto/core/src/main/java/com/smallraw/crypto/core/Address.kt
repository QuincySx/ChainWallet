package com.smallraw.crypto.core

interface Address {
    fun getFormat(): String
    fun getAddress(): ByteArray
}