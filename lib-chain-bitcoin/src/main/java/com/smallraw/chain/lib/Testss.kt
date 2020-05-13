package com.smallraw.chain.lib

import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Secp256K1.createPublicKey
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.jni.CryptoJNI
import com.smallraw.chain.lib.util.*
import java.util.*

object Testss {
    fun test() {

        timeDiff {
            start()
            var publicKey = createPublicKey(
                "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToBytes()
            )
            //45958b1c3debd87feb88c7a2cf471732d3fc1e9e2d9f43f1efb1497541cd800f475fc094940d088dfc6948429b0427b9ab520d8c062a4b55dd69a0ca920a5e937
            pause("create public key")
            publicKey = publicKey ?: byteArrayOf()
            pause("public key if null")
            val publicKeyHex = publicKey.toHex()
            pause("public key to hex")
            val publickey = publicKeyHex.hexToBytes()
            pause("public key to bytes")
            end()
            println(publicKeyHex)
        }

        val bytes = ByteArray(32)
        Random().nextBytes(bytes)
        createPublicKey(bytes)

        timeDiff {
            val hexToBytes =
                "12345f".hexToBytes()

            Base58.encode(hexToBytes)

            start("Base58EncodeCheck c++")
            val encode = Base58.encode(hexToBytes)
            end()
            val decode = Base58.decode(encode!!)

            println("is run $encode")
            println("is run ${decode?.toHex()}")
        }

        timeDiff {
            val hexToBytes =
                "12345f".hexToBytes()
            start("Base58EncodeCheck java")
            val encode = Base58Util.encode(hexToBytes)
            end()
            val decode = Base58Util.decode(encode!!)

            println("is run java $encode")
            println("is run java ${decode?.toHex()}")
        }
    }
}