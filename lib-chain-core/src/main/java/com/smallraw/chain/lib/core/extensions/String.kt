package com.smallraw.chain.lib.core.extensions

import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.jni.CryptoJNI

fun String.hexToByteArray() = CryptoJNI().strToHex(this) ?: throw JNICallException()