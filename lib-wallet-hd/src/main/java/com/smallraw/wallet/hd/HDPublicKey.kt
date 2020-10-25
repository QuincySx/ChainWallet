package com.smallraw.wallet.hd

class HDPublicKey() {
    var index = 0
    var external = true

    var publicKey: ByteArray = byteArrayOf()


    constructor(index: Int, external: Boolean, key: HDKey) : this() {
        this.index = index
        this.external = external
        this.publicKey = key.getPubKey()
    }

}