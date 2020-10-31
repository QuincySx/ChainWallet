package com.smallraw.wallet.mnemonic

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MnemonicUnitTest {

    @Test
    fun mnemonic() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val random: ByteArray = RandomSeed.random(WordCount.TWELVE)
        val mnemonic = MnemonicGenerator(WordList(appContext, WordType.ENGLISH)).createMnemonic(random)
        Assert.assertNotNull(mnemonic)
    }

    @Test
    fun test_mnemonic_reset_by_three() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val mnemonicWordsInAList: MutableList<String> = ArrayList()
        mnemonicWordsInAList.add("sponsor")
        mnemonicWordsInAList.add("thrive")
        mnemonicWordsInAList.add("twin")

        val seed = SeedCalculator()
            .withWordsFromWordList(WordList(appContext, WordType.ENGLISH))
            .calculateSeed(mnemonicWordsInAList, "")

        Assert.assertEquals(
            seed.toHex(),
            "411b65ae261e383c5e3846e8290b693ae8a6c86aa55f87e946f940945f5e8476e722161351485c07fb6fe71229899d2565f114d4c84cf0dec714ee9dfb8f9bde"
        )
    }

    @Test
    fun test_mnemonic_reset_by_list() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val mnemonicWordsInAList: MutableList<String?> = ArrayList()
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
        val seed = SeedCalculator()
            .withWordsFromWordList(WordList(appContext, WordType.ENGLISH))
            .calculateSeed(mnemonicWordsInAList, "")
        val seed1 = SeedCalculator()
            .calculateSeed(mnemonicWordsInAList, "")
        Assert.assertEquals(
            seed1.toHex(),
            "165b063a8f7a58e3650534512f53ffeb2cdab1b73604ce631f5e340aa3ff266cb8811bd671ff6268d10bc64200ea671c94e35f413d130d3d9e7ee86b10021c54"
        )
    }
}