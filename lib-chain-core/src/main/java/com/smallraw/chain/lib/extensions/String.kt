package com.smallraw.chain.lib.extensions

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

fun String.hexStringToByteArray() = CryptoJNI().strToHex(this) ?: throw JNICallException()