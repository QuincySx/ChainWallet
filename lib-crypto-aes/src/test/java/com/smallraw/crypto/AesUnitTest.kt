package com.smallraw.crypto

import com.smallraw.chain.lib.extensions.hexToBytes
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.spongycastle.util.encoders.Hex
import java.io.File
import java.lang.reflect.Field


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AesUnitTest {
    @Before
    @Throws(Exception::class)
    fun setUp() {
        val dylibsDir = File(".externalNativeBuild/cmake/debug/osx")
        val libraryPath: String =
            dylibsDir.absolutePath.toString() + ":" + System.getProperty("java.library.path")
        System.setProperty("java.library.path", libraryPath)
        val fieldSysPath: Field = ClassLoader::class.java.getDeclaredField("sys_paths")
        fieldSysPath.isAccessible = true
        fieldSysPath.set(null, null)
    }

    private val tData =
        Hex.decode("AAFE47EE82411A2BF3F6752AE8D7831138F041560631B114F3F6752AE8D7831138F041560631B1145A01020304050607")
    private val outECB =
        Hex.decode("a444a9a4d46eb30cb7ed34d62873a89fae040483a55b3f0578d08bd007b3e14a763a2011fe80dd73fbad625c3607b3fa")
    private val outCBC1 =
        Hex.decode("a444a9a4d46eb30cb7ed34d62873a89f8fdf2bf8a54e1aeadd06fd85c9cb46f021ee7cd4f418fa0bb72e9d07c70d5d20")
    private val outCBC2 =
        Hex.decode("585681354f0e01a86b32f94ebb6a675045d923cf201263c2aaecca2b4de82da0edd74ca5efd654c688f8a58e61955b11")
    private val outCTR1 =
        Hex.decode("82a1744e8ebbd053ca72362d5e570326e0b6fdaf824ab673fbf029042886b23c75129a015852913790f81f94447475a0")
    private val outCTR2 =
        Hex.decode("146cbb581d9e12c3333dd9c736fbb93043c92019f78580da48f81f80b3f551d58ea836fed480fc6912fefa9c5c89cc24")
    private val outCFB1 =
        Hex.decode("82a1744e8ebbd053ca72362d5e5703264b4182de3208c374b8ac4fa36af9c5e5f4f87d1e3b67963d06acf5eb13914c90")
    private val outCFB2 =
        Hex.decode("146cbb581d9e12c3333dd9c736fbb9303c8a3eb5185e2809e9d3c28e25cc2d2b6f5c11ee28d6530f72c412b1438a816a")
    private val outOFB1 =
        Hex.decode("82a1744e8ebbd053ca72362d5e5703261ebf1fdbec05e57b3465b583132f84b43bf95b2c89040ad1677b22d42db69a7a")
    private val outOFB2 =
        Hex.decode("146cbb581d9e12c3333dd9c736fbb9309ea4c2a7696c84959a2dada49f2f1c5905db1f0cec3a31acbc4701e74ab05e1f")

    @Test
    fun aes_ecb_encode() {
        val aes = Aes()

        val encrypt = aes.encryptECB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
        )
        Assert.assertArrayEquals(outECB, encrypt)


        val encrypt1 = aes.encryptECB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = "112233".hexToBytes(),
        )
        Assert.assertArrayEquals("73e745b400eddfb75bb8130231aa5bd7".hexToBytes(), encrypt1)
    }

    @Test
    fun aes_ecb_decode() {
        val aes = Aes()

        val decrypt = aes.decryptECB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outECB,
        )
        Assert.assertArrayEquals(tData, decrypt)


        val decrypt1 = aes.decryptECB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = "73e745b400eddfb75bb8130231aa5bd7".hexToBytes(),
        )
        Assert.assertArrayEquals("112233".hexToBytes(), decrypt1)
    }

    @Test
    fun aes_cbc_encode() {
        val aes = Aes()

        val encrypt1 = aes.encryptCBC(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(outCBC1, encrypt1)

        val encrypt2 = aes.encryptCBC(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(outCBC2, encrypt2)

        val encrypt3 = aes.encryptCBC(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = "112233".hexToBytes(),
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals("07A2BB5380BAFFE60F3DB01F702F6415".hexToBytes(), encrypt3)
    }

    @Test
    fun aes_cbc_decode() {
        val aes = Aes()

        val decrypt1 = aes.decryptCBC(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outCBC1,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(tData, decrypt1)

        val decrypt2 = aes.decryptCBC(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outCBC2,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(tData, decrypt2)

        val decrypt3 = aes.decryptCBC(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = "07A2BB5380BAFFE60F3DB01F702F6415".hexToBytes(),
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals("112233".hexToBytes(), decrypt3)
    }

    @Test
    fun aes_cfb_encode() {
        val aes = Aes()

        val encrypt1 = aes.encryptCFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(outCFB1, encrypt1)

        val encrypt2 = aes.encryptCFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(outCFB2, encrypt2)
    }

    @Test
    fun aes_cfb_decode() {
        val aes = Aes()

        val encrypt1 = aes.decryptCFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outCFB1,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(tData, encrypt1)

        val encrypt2 = aes.decryptCFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outCFB2,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(tData, encrypt2)
    }

    @Test
    fun aes_ofb_encode() {
        val aes = Aes()

        val encrypt1 = aes.encryptOFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(outOFB1, encrypt1)

        val encrypt2 = aes.encryptOFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(outOFB2, encrypt2)
    }

    @Test
    fun aes_ofb_decode() {
        val aes = Aes()

        val encrypt1 = aes.decryptOFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outOFB1,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(tData, encrypt1)

        val encrypt2 = aes.decryptOFB(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outOFB2,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(tData, encrypt2)
    }

    @Test
    fun aes_ctr_encode() {
        val aes = Aes()

        val encrypt1 = aes.encryptCTR(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(outCTR1, encrypt1)

        val encrypt2 = aes.encryptCTR(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = tData,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(outCTR2, encrypt2)
    }

    @Test
    fun aes_ctr_decode() {
        val aes = Aes()

        val encrypt1 = aes.decryptCTR(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outCTR1,
            iv = ByteArray(16),
        )
        Assert.assertArrayEquals(tData, encrypt1)

        val encrypt2 = aes.decryptCTR(
            key = "5F060D3716B345C253F6749ABAC10917".hexToBytes(),
            data = outCTR2,
            iv = "000102030405060708090a0b0c0d0e0f".hexToBytes(),
        )
        Assert.assertArrayEquals(tData, encrypt2)
    }
}