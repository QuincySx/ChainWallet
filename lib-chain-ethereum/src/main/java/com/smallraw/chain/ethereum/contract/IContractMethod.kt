package com.smallraw.chain.ethereum.contract

import com.smallraw.chain.ethereum.abi.Abi

abstract class IContractMethod {
    protected abstract val methodName: String
    protected abstract val methodParams: Array<String>

    val methodId: ByteArray by lazy {
        Abi.encodeMethodId(methodName, methodParams)
    }

    fun encodedABI(): ByteArray {
        return methodId + Abi.encodeParams(methodParams, getArguments())
    }

    protected abstract fun getArguments(): Array<Any>
}