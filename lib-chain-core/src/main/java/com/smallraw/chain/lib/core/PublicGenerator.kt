package com.smallraw.chain.lib.core

import java.security.KeyPair
import java.security.PrivateKey

interface PublicGenerator {
    fun generate(privateKey: PrivateKey): KeyPair
}