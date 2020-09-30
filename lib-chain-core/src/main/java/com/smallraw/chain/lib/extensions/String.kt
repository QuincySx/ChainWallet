package com.smallraw.chain.lib.extensions

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

fun String.hexToByteArray() = CryptoJNI().strToHex(this) ?: throw JNICallException()