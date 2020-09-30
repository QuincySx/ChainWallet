package com.smallraw.chain.lib.bitcoin.network

import com.smallraw.chain.lib.bitcoin.transaction.script.SigHash
import com.smallraw.chain.lib.extensions.hexToByteArray

abstract class BaseNetwork {
    val zeroHashBytes =
        "0000000000000000000000000000000000000000000000000000000000000000".hexToByteArray()

    abstract var bip32HeaderPub: Int
    abstract var bip32HeaderPriv: Int
    abstract var coinType: Int
    abstract var addressVersion: Int
    abstract var addressWifVersion: Int
    abstract var addressSegwitHrp: String
    abstract var addressScriptVersion: Int

//    open val bip44Checkpoint = Checkpoint("${javaClass.simpleName}-bip44.checkpoint")
//    open val lastCheckpoint = Checkpoint("${javaClass.simpleName}.checkpoint")

    open val sigHashForked: Boolean = false
    open val sigHashValue = SigHash.ALL
}