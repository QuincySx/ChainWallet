package com.smallraw.crypto.core

import java.security.KeyPair
import java.security.PrivateKey

interface PublicGenerator {
    fun generate(privateKey: PrivateKey): KeyPair
}