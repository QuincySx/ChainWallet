package com.smallraw.chain.ethereum

import com.smallraw.chain.ethereum.network.MainNet
import com.smallraw.chain.ethereum.supplement.EIP55
import com.smallraw.chain.ethereum.supplement.ERC1191
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test


class AddressUnitTest {
    @Test
    fun test_address() {
        val privateKey =
            PrivateKey.ofHex("89afc24b157548633b6e54e4e7e6f00096cfb0e750854914472fc6571306e849")
        val publicKey = privateKey.getPublicKey()
        val address = publicKey.getAddress()

        Assert.assertEquals(
            publicKey.getKey().toHex(),
            "f06fb2e64d30e7e31342ac8ec40f58ea4945e1feab04a98c8388a62136e20e62452b5b580e66311c4689edfdfffa437a240e6352a37bad914a2be73a42e2c00f"
        )
        Assert.assertEquals(address.hex, "0x4626f9ea04267a7ff2904f9e6808bd7042cff858")
    }

    @Test
    fun test_eip55() {
        val formatAddress = EIP55.format("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB")
        val verify = EIP55.verify(formatAddress)
        Assert.assertTrue(verify)
    }

    @Test
    fun test_eip1191() {
        val network = MainNet()
        val formatAddress = ERC1191.format("0x42712D45473476b98452f434e72461577D686318", network)
        val verify = ERC1191.verify(formatAddress, network)
        Assert.assertTrue(verify)
    }
}