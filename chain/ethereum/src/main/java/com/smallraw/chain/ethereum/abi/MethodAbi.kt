package com.smallraw.chain.ethereum.abi

class MethodAbi(private val methodName: String, private val params: Array<String>) {
    fun getMethodId(): ByteArray {
        return Abi.encodeMethodId(methodName, params)
    }

    fun encode(args: Array<Any>): ByteArray {
        return getMethodId() + Abi.encodeParams(params, args)
    }
}