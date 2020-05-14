package com.smallraw.chain.lib.util

import com.smallraw.chain.lib.jni.CryptoJNI

fun ByteArray.toHex() = CryptoJNI.hexToStr(this, this.size)

fun String.hexToBytes() = CryptoJNI.strToHex(this)