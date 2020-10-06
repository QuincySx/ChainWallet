package com.smallraw.chain.bitcoin.convert

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.bitcoin.transaction.script.ScriptType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressConvertUnitTest {

    @Test
    fun test_p2wpkh(){
        val addressString = "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4"
        val program = "751e76e8199196d454941c45d1b3a323f1433bd6".hexToByteArray()
        val bytes = "0014".hexToByteArray() + program

        val converter = SegwitAddressConverter("bc")
        val address = converter.convert(bytes, ScriptType.P2WPKH)

        Assert.assertEquals(Bitcoin.Address.AddressType.WITNESS, address.type)
        Assert.assertEquals(addressString, address.address)
        Assert.assertArrayEquals(program, address.hashKey)
    }

    @Test
    fun test_p2wsh(){
        val addressString = "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sl5k7"
        val program = "1863143c14c5166804bd19203356da136c985678cd4d27a1b8c6329604903262".hexToByteArray()
        val bytes = "00201863143c14c5166804bd19203356da136c985678cd4d27a1b8c6329604903262".hexToByteArray()

        val converter = SegwitAddressConverter("tb")
        val address = converter.convert(bytes, ScriptType.P2WSH)

        Assert.assertEquals(Bitcoin.Address.AddressType.WITNESS, address.type)
        Assert.assertEquals(addressString, address.address)
        Assert.assertArrayEquals(program, address.hashKey)
    }

    @Test
    fun test_witness1(){
        val addressString = "bc1pw508d6qejxtdg4y5r3zarvary0c5xw7kw508d6qejxtdg4y5r3zarvary0c5xw7k7grplx"
        val program = "751e76e8199196d454941c45d1b3a323f1433bd6751e76e8199196d454941c45d1b3a323f1433bd6".hexToByteArray()
        val bytes = "5128751e76e8199196d454941c45d1b3a323f1433bd6751e76e8199196d454941c45d1b3a323f1433bd6".hexToByteArray()

        val converter = SegwitAddressConverter("bc")
        val address = converter.convert(bytes, ScriptType.P2WPKH)

        Assert.assertEquals(Bitcoin.Address.AddressType.WITNESS, address.type)
        Assert.assertEquals(addressString, address.address)
        Assert.assertArrayEquals(program, address.hashKey)
    }
}