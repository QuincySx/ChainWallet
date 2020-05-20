package com.smallraw.chain.lib

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.crypto.Curve25519
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import com.smallraw.chain.lib.extensions.toHex
import com.smallraw.chain.lib.util.timeDiff
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Curve25519UnitTest {
    @Test
    fun create_private() {
        // Context of the app under test.
        timeDiff {
            start("is run ed25519 private")
            val createPrivateKey = Curve25519.createPrivateKey()
            pause()
            println("is run privateKey ${createPrivateKey.toHex()}")
            pause()
            val createPublicKey = Curve25519.createPublicKey(createPrivateKey)
            pause()
            end()
            println("is run publicKey ${createPublicKey!!.toHex()}")
        }
    }

    @Test
    fun create_public() {
        // Context of the app under test.
        timeDiff {
            start("is run curve25519 public")
            val createPrivateKey =
                "6062636465666768696a6b6c6d6e6f707172737475767778797a313233343576".hexStringToByteArray()!!
            pause()
            val createPublicKey = Curve25519.createPublicKey(createPrivateKey)
            pause()
            end()
            println("is run publicKey ${createPublicKey!!.toHex()}")
            Assert.assertEquals(
                createPublicKey!!.toHex(),
                "c21365f308f2dd95824ba1a612b158f86522af229f48a0346904dfb1e16e2478"
            )
        }
    }

    @Test
    fun create_share() {
        // Context of the app under test.
        val message = "abcd".hexStringToByteArray()!!
        timeDiff {
            start("is run curve25519 share")
            val privateKey1 =
                "6062636465666768696a6b6c6d6e6f707172737475767778797a313233343576".hexStringToByteArray()!!
            val privateKey2 =
                "3035343332317a797877767574737271706f6e6d6c6b6a696867666564636261".hexStringToByteArray()!!
            pause()
            val createPublicKey1 = Curve25519.createPublicKey(privateKey1)!!
            pause()
            val createPublicKey2 = Curve25519.createPublicKey(privateKey2)!!
            pause()
            val shareKey12 = Curve25519.createShareKey(privateKey1, createPublicKey2)
            pause()
            val shareKey21 = Curve25519.createShareKey(privateKey2, createPublicKey1)
            pause()
            end()
            println("is run public key 1 ${createPublicKey1.toHex()}")
            println("is run public key 2 ${createPublicKey2.toHex()}")
            println("is run share key 12 ${shareKey12!!.toHex()}")
            println("is run share key 21 ${shareKey21!!.toHex()}")
            Assert.assertEquals(
                shareKey12.toHex(),
                "b0818125eab42a8ac1af5e8b9b9c15ed2605c2bbe9675de89e5e6e7f442b9598"
            )
            Assert.assertEquals(
                shareKey21.toHex(),
                "b0818125eab42a8ac1af5e8b9b9c15ed2605c2bbe9675de89e5e6e7f442b9598"
            )
        }
    }
}