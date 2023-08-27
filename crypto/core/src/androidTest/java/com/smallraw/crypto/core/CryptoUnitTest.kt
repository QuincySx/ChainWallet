package com.smallraw.crypto.core

import com.smallraw.crypto.core.crypto.BLAKE2B
import com.smallraw.crypto.core.crypto.BLAKE2S
import com.smallraw.crypto.core.crypto.Base32
import com.smallraw.crypto.core.crypto.Base58
import com.smallraw.crypto.core.crypto.Blake
import com.smallraw.crypto.core.crypto.HmacSha2
import com.smallraw.crypto.core.crypto.Keccak
import com.smallraw.crypto.core.crypto.Pbkdf2
import com.smallraw.crypto.core.crypto.Ripemd160
import com.smallraw.crypto.core.crypto.Sha256
import com.smallraw.crypto.core.crypto.Sha3
import com.smallraw.crypto.core.crypto.Sha512
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//@RunWith(AndroidJUnit4::class)
class CryptoUnitTest {
    private val TAG = "ExampleInstrumentedTest"

    //    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.mylibrary.test", appContext.packageName)
//    }
    @Test
    fun base32() {
        val date =
            "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToByteArray()
        val encode = Base32.encode(date)
        assertEquals(encode, "JVK46E4JTQDZY7WT6PEXHKB2KRCR3TYXNZLZ6YMV3INFN3K74BKA====")
        val decode = Base32.decode(encode)
        assertEquals(decode.toHex(), "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054000000")

        val encodeNoPadding = Base32.encode(date, false)
        assertEquals(encodeNoPadding, "JVK46E4JTQDZY7WT6PEXHKB2KRCR3TYXNZLZ6YMV3INFN3K74BKA")
        val decodeNoPadding = Base32.decode(encodeNoPadding, false)
        assertArrayEquals(decodeNoPadding, date)
    }

    @Test
    fun base58() {
        val date =
            "4d55cf13899c079c7ed3f3c973a83a54451dcf176e579f6195da1a56ed5fe054".hexToByteArray()
        val encode = Base58.encode(date)
        assertEquals(encode, "6CtHPVEH56orAaTL6M4UC6o8nF1f1Z5TPPQdGFWuzsqd")
        val decode = Base58.decode(encode)
        assertArrayEquals(decode, date)
    }

    @Test
    fun base58_check() {
        val date = "abcd".hexToByteArray()
        val encode = Base58.encodeCheck(date)
        assertEquals(encode, "2UZ1mCYWH")
        val decode = Base58.decodeCheck(encode)
        assertArrayEquals(decode, date)
    }

    @Test
    fun sha256() {
        val sha256 = Sha256.sha256("123".toByteArray()).toHex()
        assertEquals(sha256, "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3")
        val doubleSha256 =
            Sha256.doubleSha256("abcd".hexToByteArray()).toHex()
        assertEquals(
            doubleSha256,
            "179980f6862aedb22205ac97c8af29c77e25d02e189b52926bb1d93796bb3c94"
        )
    }

    @Test
    fun sha512() {
        val sha512 = Sha512.sha512("123".toByteArray()).toHex()
        assertEquals(
            sha512,
            "3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2"
        )
    }

    @Test
    fun ripemd160() {
        val data = "abcd".hexToByteArray()
        val hash = Ripemd160.ripemd160(data).toHex()
        assertEquals(
            hash,
            "a21c2817130deaa1105afb3b858dbd219ee2da44"
        )
    }

    @Test
    fun sha3_256() {
        val data = "abcd".hexToByteArray()
        val hash = Sha3.sha256(data).toHex()
        assertEquals(
            hash,
            "0f1108bfb4ddb5cd6a8b05ad6dbc8244f0b0ef94cf77475a60a7bc952058425b"
        )

        val doubleHash = Sha3.doubleSha256(data).toHex()
        assertEquals(
            doubleHash,
            "0e55640509c955154be3b6f05f655b99d475199a0c4d044ef6f2bcbae4630d0c"
        )

        val hash224 = Sha3.sha224(data).toHex()
        assertEquals(
            hash224,
            "93832aee843c16d1f50b3e008e73898d3c238078da8bae62585369e1"
        )

        val sha384 = Sha3.sha384(data).toHex()
        assertEquals(
            sha384,
            "771cbe61fa0630dea6c8e7cbd9ff326fc5c2d5f2019c65f78f8c0a1894526543a20ee44dd7875f23071b8b3ea9675899"
        )

        val sha512 = Sha3.sha512(data).toHex()
        assertEquals(
            sha512,
            "0a1391299f4fe72fa915fad2263647c8b2ab868517093cf08c6c432a4d6b015f147fab214b8182d5b01309f15812321e2593a3dc2a32a0dc3bb7023b735ad854"
        )
    }

    @Test
    fun keccak_256() {
        val data = "abcd".hexToByteArray()
        val hash = Keccak.sha256(data).toHex()
        assertEquals(
            hash,
            "dbe576b4818846aa77e82f4ed5fa78f92766b141f282d36703886d196df39322"
        )

        val doubleHash = Keccak.doubleSha256(data).toHex()
        assertEquals(
            doubleHash,
            "d33e7c66e18c0118da21e9a35495306a28bb2e278781352ee93c962c19b47452"
        )

        val sha224 = Keccak.sha224(data).toHex()
        assertEquals(
            sha224,
            "247f3d11b836489d749c371ef8f00ad0213133a2b0d5a63ac13b8190"
        )

        val sha384 = Keccak.sha384(data).toHex()
        assertEquals(
            sha384,
            "e3d601c3513f228851c198285238db2bae2f54554e5d3b7a232a92553cf8637c26ae6a9aa53809a6f5c3d0d909a1df7a"
        )

        val sha512 = Keccak.sha512(data).toHex()
        assertEquals(
            sha512,
            "6396d7ff9fff08e45e02e1bfa2ad5711324be71bb6b02a745efae0590830501e758b7863cf4c9174ceea4d2019bb33d28acd41b4011350010a386b6e223db312"
        )
    }

    @Test
    fun hmac_sha2() {
        val sha256 = HmacSha2.sha256("Bitcoin seed".toByteArray(), "abcd".toByteArray()).toHex()
        assertEquals(sha256, "860f28d55c12f1162a3fd482c9da3c86ba8c7d772f2be6f94d030057ff9168b3")

        val sha512 = HmacSha2.sha512("Bitcoin seed".toByteArray(), "abcd".toByteArray()).toHex()
        assertEquals(
            sha512,
            "9ff7be0af575c24466b96190b97034972c781bf4204cf5531d6eece7b8ec942b8b5a71d9158eae9b6121c3f322a661e1d6c5362859d78b5c553525c30cc0e56e"
        )
    }

    @Test
    fun black() {
        val data = "abcd".toByteArray()
        val key = "010203".toByteArray()
        val blake2b_256 = Blake.blake2b(data, outlen = BLAKE2B.BLAKE2B_256).toHex()
        assertEquals(blake2b_256, "9cc3912a042827e45983ed53df3c759f4574added1d07c6d0c7fe0bc3ecf9c42")

        val blake2b_256_key = Blake.blake2b(data, key, outlen = BLAKE2B.BLAKE2B_256).toHex()
        assertEquals(blake2b_256_key, "136c84073c2167146e7f5136f2276cea8646cc98cd96ed24fd348e875b22ec95")

        val blake2b_384 = Blake.blake2b(data, outlen = BLAKE2B.BLAKE2B_384).toHex()
        assertEquals(blake2b_384, "db6895d33d46005187ee1f31b40e5606d80f6b4245e7a3f67ce0c48c2287b9bd1cc1a79e7fef602a534a5677ebbb62b8")

        val blake2b_384_key = Blake.blake2b(data, key, outlen = BLAKE2B.BLAKE2B_384).toHex()
        assertEquals(blake2b_384_key, "c8bc54f7204dc7e8cb57ba47308f68f195281b57499330e0e1e3cf29e6ab21254f14d1083a62713fd631a5b9b8d14279")

        val blake2b_512 = Blake.blake2b(data, outlen = BLAKE2B.BLAKE2B_512).toHex()
        assertEquals(
            blake2b_512,
            "26bc14024d5d6818ad7c4dee519353c290e38b6535f16f62b6ce5c6ff346c354542496f89b84eacffa1da51f0ac5e643f965637cc24e0b3f819bdae05f3932b0"
        )

        val blake2b_512_key = Blake.blake2b(data, key, outlen = BLAKE2B.BLAKE2B_512).toHex()
        assertEquals(
            blake2b_512_key,
            "dab3df8553557e2e1a41b2c30bebdf7543f6334c3739a5fd543ba2984ef3b15fedb06c509dea1f07c16995c739c98a8dd9a75381b9b25d37053947c4058b489c"
        )

        val blake2s_128 = Blake.blake2s(data, outlen = BLAKE2S.BLAKE2S_128).toHex()
        assertEquals(blake2s_128, "13d1e3a805a9b3951862c51d4f829358")

        val blake2s_128_key = Blake.blake2s(data, key, outlen = BLAKE2S.BLAKE2S_128).toHex()
        assertEquals(blake2s_128_key, "8284fe7f13e9c864f8c8fa946b40380f")

        val blake2s_256 = Blake.blake2s(data, outlen = BLAKE2S.BLAKE2S_256).toHex()
        assertEquals(blake2s_256, "716748cce97a0abc942e1d491bc25102f5b6ff71ee62a86abd605a6c40120169")

        val blake2s_256_key = Blake.blake2s(data, key, outlen = BLAKE2S.BLAKE2S_256).toHex()
        assertEquals(blake2s_256_key, "a6ac312f4895970405a318d9002324addf2ace192a055235efe0000056a94d86")

        val blake256 = Blake.blake256(data).toHex()
        assertEquals(blake256, "35282468f3b93c5aaca6408582fced36e578f67671ed0741c332d68ac72d7aa2")
    }

    @Test
    fun pbkdf2Hmac() {
        val data = "abcd".toByteArray()
        val salt = "010203".toByteArray()

        val pbkdf2_hmac_sha256 = Pbkdf2.hmacSha256(data, salt, 1000).toHex()
        assertEquals(
            pbkdf2_hmac_sha256,
            "8c5e22ad1e3afaaf6fd94e117d526f86ce04a89c611c3b49163d4e0ff90e125f"
        );

        val pbkdf2_hmac_sha512 = Pbkdf2.hmacSha512(data, salt, 1000).toHex()
        assertEquals(
            pbkdf2_hmac_sha512,
            "cf5b79ef48d0eec42a480a475d22f82ed46fe48849b7915b83d477644a012efc048f4f85f8e266121bde72dc60c00ee61a7f9d6b4b654b2a5e363b7c1ffea7ef"
        )
    }
}