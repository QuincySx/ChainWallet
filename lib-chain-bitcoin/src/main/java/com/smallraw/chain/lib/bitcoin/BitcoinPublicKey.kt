package com.smallraw.chain.lib.bitcoin

interface BitcoinPublicKey {
    //    companion object{
//        fun from(){
//
//        }
//    }
    fun getHash(): ByteArray

    fun getFormat(): ByteArray

    fun getPublicKey(): ByteArray

    fun scriptHashP2WPKH(): ByteArray
}