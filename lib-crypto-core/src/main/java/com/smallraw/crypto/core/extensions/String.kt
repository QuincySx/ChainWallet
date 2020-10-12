package com.smallraw.crypto.core.extensions

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

fun String.hexToByteArray() = CryptoJNI().strToHex(this) ?: throw JNICallException()