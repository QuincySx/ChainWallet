package com.smallraw.chain.ethereum

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressUnitTest {
    @Test
    fun test_address() {
        val privateKey =
            PrivateKey.ofHex("89afc24b157548633b6e54e4e7e6f00096cfb0e750854914472fc6571306e849")
        val publicKey = privateKey.getPublicKey()
        val address= publicKey.getAddress()

        Assert.assertEquals(publicKey.getKey().toHex(), "f06fb2e64d30e7e31342ac8ec40f58ea4945e1feab04a98c8388a62136e20e62452b5b580e66311c4689edfdfffa437a240e6352a37bad914a2be73a42e2c00f")
        Assert.assertEquals(address.hex, "0x4626f9ea04267a7ff2904f9e6808bd7042cff858")
    }
}