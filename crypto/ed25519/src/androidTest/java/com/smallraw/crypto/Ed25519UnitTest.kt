package com.smallraw.crypto

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import com.smallraw.crypto.core.util.timeDiff
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


class Ed25519UnitTest {
    @Test
    fun create_ed25519_private() {
        // Context of the app under test.
        timeDiff {
            start("is run ed25519 private")
            val createPrivateKey = Ed25519.createPrivateKey()
            pause()
            println("is run privateKey ${createPrivateKey.toHex()}")
            pause()
            val createPublicKey = Ed25519.createPublicKey(createPrivateKey)
            pause()
            end()
            println("is run publicKey ${createPublicKey.toHex()}")
        }
    }

    @Test
    fun create_ed25519_public() {
        // Context of the app under test.
        timeDiff {
            start("is run ed25519 public")
            val createPrivateKey =
                "4a16668abe95d13a6c79f274a37bcbf486a193e800031790e338f7c8be7db480".hexToByteArray()
            pause()
            val createPublicKey = Ed25519.createPublicKey(createPrivateKey)
            pause()
            end()
            println("is run publicKey ${createPublicKey.toHex()}")
            Assert.assertEquals(
                createPublicKey.toHex(),
                "b56be88d86cea6325e653135fe12169b1ac8dc5085c0bfdf4f31f96a37d60778"
            )
        }
    }

    @Test
    fun create_ed25519_sign() {
        // Context of the app under test.
        val message = "abcd".hexToByteArray()
        timeDiff {
            start("is run ed25519 public")
            val createPrivateKey =
                "e74ee6be66b9144e7cb20cb1496f946d9a7541c0cd80cf6e98be75d763af7800".hexToByteArray()
            pause()
            val createPublicKey = Ed25519.createPublicKey(createPrivateKey)
            pause()
            val signature = Ed25519.sign(createPrivateKey, message)
            pause()
            end()
            println("is run publicKey ${createPublicKey.toHex()}")
            Assert.assertEquals(
                createPublicKey.toHex(),
                "2231e5b9159e026553a86895715cb811079b6f3d3e776c88ec5457d86b3d9331"
            )
            println("is run signature ${signature.toHex()}")
            Assert.assertEquals(
                signature.toHex(),
                "4871123da4a40ab26a4dc0a5bb5a57818eb4819b9ed28080751dd221ca636f123d3f2caa83645fc723bbb23580c650e8ead9dbf8cc5d9b1a44c6817ea9e7ee08"
            )
        }
    }

    @Test
    fun create_ed25519_verify() {
        // Context of the app under test.
        val message = "abcd".hexToByteArray()
        timeDiff {
            start("is run ed25519 public")
            val createPrivateKey =
                "e74ee6be66b9144e7cb20cb1496f946d9a7541c0cd80cf6e98be75d763af7800".hexToByteArray()
            pause()
            val createPublicKey = Ed25519.createPublicKey(createPrivateKey)
            pause()
            val signature = Ed25519.sign(createPrivateKey, message)
            pause()
            val verify = Ed25519.verify(createPublicKey, signature, message)
            pause()
            end()
            println("is run publicKey ${createPublicKey.toHex()}")
            Assert.assertEquals(
                createPublicKey.toHex(),
                "2231e5b9159e026553a86895715cb811079b6f3d3e776c88ec5457d86b3d9331"
            )
            println("is run signature ${signature.toHex()}")
            Assert.assertEquals(
                signature.toHex(),
                "4871123da4a40ab26a4dc0a5bb5a57818eb4819b9ed28080751dd221ca636f123d3f2caa83645fc723bbb23580c650e8ead9dbf8cc5d9b1a44c6817ea9e7ee08"
            )
            println("is run signature $verify")
            Assert.assertEquals(
                verify,
                true
            )
        }
    }
}