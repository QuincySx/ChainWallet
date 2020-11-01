package com.smallraw.wallet.mnemonic

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class MnemonicUnitTest {

    @Test
    fun mnemonic() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mnemonicBuild = MnemonicBuild(appContext)
        val newFixedThreadPool = Executors.newFixedThreadPool(32)
        for (i in 0..100) {
            newFixedThreadPool.submit {
                val mnemonic = mnemonicBuild.createMnemonic(WordCount.TWELVE, WordType.ENGLISH)
                Assert.assertNotNull(mnemonic)
                Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonic))
            }
        }

        for (i in 0..100) {
            newFixedThreadPool.submit {
                val mnemonic = mnemonicBuild.createMnemonic(WordCount.EIGHTEEN, WordType.JAPANESE)
                Assert.assertNotNull(mnemonic)
                Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonic))
            }
        }

        for (i in 0..100) {
            newFixedThreadPool.submit {
                val mnemonic =
                    mnemonicBuild.createMnemonic(WordCount.TWELVE, WordType.CHINESE_TRADITIONAL)
                Assert.assertNotNull(mnemonic)
                Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonic))
            }
        }

        for (i in 0..100) {
            newFixedThreadPool.submit {
                val mnemonic = mnemonicBuild.createMnemonic(WordCount.TWENTY_FOUR, WordType.ENGLISH)
                Assert.assertNotNull(mnemonic)
                Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonic))
            }
        }
    }

    @Test
    fun test_mnemonic_reset_by_three() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mnemonicBuild = MnemonicBuild(appContext)

        val mnemonicWordsInAList: MutableList<String> = ArrayList()
        mnemonicWordsInAList.add("sponsor")
        mnemonicWordsInAList.add("thrive")
        mnemonicWordsInAList.add("twin")

        val seed = mnemonicBuild.createSeedByMnemonic(mnemonicWordsInAList, ">>》")

        Assert.assertEquals(
            seed.toHex(),
            "7caddff1bad258a404684926b512c0a63af3ebbdcc538b1666311583353792aff0143a3a7184030023d3a1617945185a3c791c5031e0949360154e0d3395aa94"
        )

        Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonicWordsInAList))
    }

    @Test
    fun test_mnemonic_reset_by_list() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mnemonicBuild = MnemonicBuild(appContext)

        val mnemonicWordsInAList: MutableList<String> = ArrayList()
        mnemonicWordsInAList.add("cupboard")
        mnemonicWordsInAList.add("shed")
        mnemonicWordsInAList.add("accident")
        mnemonicWordsInAList.add("simple")
        mnemonicWordsInAList.add("marble")
        mnemonicWordsInAList.add("drive")
        mnemonicWordsInAList.add("put")
        mnemonicWordsInAList.add("crew")
        mnemonicWordsInAList.add("marine")
        mnemonicWordsInAList.add("mistake")
        mnemonicWordsInAList.add("shop")
        mnemonicWordsInAList.add("chimney")
        mnemonicWordsInAList.add("plate")
        mnemonicWordsInAList.add("throw")
        mnemonicWordsInAList.add("cable")

        val seed1 = mnemonicBuild.createSeedByMnemonic(mnemonicWordsInAList, "")
        Assert.assertEquals(
            seed1.toHex(),
            "165b063a8f7a58e3650534512f53ffeb2cdab1b73604ce631f5e340aa3ff266cb8811bd671ff6268d10bc64200ea671c94e35f413d130d3d9e7ee86b10021c54"
        )

        Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonicWordsInAList))
    }

    @Test
    fun test_mnemonic_first_word_reset_by_list() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mnemonicBuild = MnemonicBuild(appContext)

        val mnemonicWordsInAList: MutableList<String> = ArrayList()
        mnemonicWordsInAList.add("cupboa")
        mnemonicWordsInAList.add("shed")
        mnemonicWordsInAList.add("accide")
        mnemonicWordsInAList.add("simple")
        mnemonicWordsInAList.add("marble")
        mnemonicWordsInAList.add("drive")
        mnemonicWordsInAList.add("put")
        mnemonicWordsInAList.add("crew")
        mnemonicWordsInAList.add("marine")
        mnemonicWordsInAList.add("mista")
        mnemonicWordsInAList.add("shop")
        mnemonicWordsInAList.add("chim")
        mnemonicWordsInAList.add("plat")
        mnemonicWordsInAList.add("thro")
        mnemonicWordsInAList.add("cabl")

        val seed1 = mnemonicBuild.createSeedByMnemonic(WordType.ENGLISH, mnemonicWordsInAList, "")
        Assert.assertEquals(
            seed1.toHex(),
            "165b063a8f7a58e3650534512f53ffeb2cdab1b73604ce631f5e340aa3ff266cb8811bd671ff6268d10bc64200ea671c94e35f413d130d3d9e7ee86b10021c54"
        )

        Assert.assertTrue(mnemonicBuild.validateMnemonic(mnemonicWordsInAList))
    }
}