package com.smallraw.chain.lib.extensions

import com.smallraw.chain.lib.utils.toHEX

/**
 * 16 进制数组转成 16 进制字符串
 */
fun ByteArray.toHex() = toHEX(this)