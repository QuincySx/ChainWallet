package com.smallraw.chain.lib.extensions

import com.smallraw.chain.lib.utils.hexToBytes

/**
 * 16 进制字符串转换16 进制数组
 */
fun String.hexToBytes() = hexToBytes(this)