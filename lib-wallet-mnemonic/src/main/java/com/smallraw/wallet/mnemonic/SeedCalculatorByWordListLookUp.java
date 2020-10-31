package com.smallraw.wallet.mnemonic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author QuincySx
 * @date 2018/5/15 上午10:39
 */
public final class SeedCalculatorByWordListLookUp {
    private final SeedCalculator seedCalculator;
    private final Map<CharSequence, char[]> map = new HashMap<>();
    private final NFKDNormalizer normalizer;

    SeedCalculatorByWordListLookUp(final SeedCalculator seedCalculator, final WordList wordList) {
        this.seedCalculator = seedCalculator;
        normalizer = new WordListMapNormalization(wordList);
        for (int i = 0; i < 1 << 11; i++) {
            final String word = normalizer.normalize(wordList.getWord(i));
            map.put(word, word.toCharArray());
        }
    }

    /**
     * Calculate the seed given a mnemonic and corresponding passphrase.
     * The phrase is not checked for validity here, for that use a {@link MnemonicValidator}.
     * <p>
     * The purpose of this method is to avoid constructing a mnemonic String if you have gathered a list of
     * words from the user and also to avoid having to normalize it, all words in the {@link WordList} are normalized
     * instead.
     * <p>
     * Due to normalization, the passphrase still needs to be {@link String}, and not {@link CharSequence}, this is an
     * open issue: https://github.com/NovaCrypto/BIP39/issues/7
     *
     * @param mnemonic   The memorable list of words, ideally selected from the word list that was supplied while creating this object.
     * @param passphrase An optional passphrase, use "" if not required
     * @return a seed for HD wallet generation
     */
    public byte[] calculateSeed(final Collection<? extends CharSequence> mnemonic, final String passphrase) {
        final int words = mnemonic.size();
        final char[][] chars = new char[words][];
        final List<char[]> toClear = new LinkedList<>();
        int count = 0;
        int wordIndex = 0;
        for (final CharSequence word : mnemonic) {
            char[] wordChars = map.get(normalizer.normalize(word));
            if (wordChars == null) {
                wordChars = normalizer.normalize(word).toCharArray();
                toClear.add(wordChars);
            }
            chars[wordIndex++] = wordChars;
            count += wordChars.length;
        }
        count += words - 1;
        final char[] mnemonicChars = new char[count];
        try {
            int index = 0;
            for (int i = 0; i < chars.length; i++) {
                System.arraycopy(chars[i], 0, mnemonicChars, index, chars[i].length);
                index += chars[i].length;
                if (i < chars.length - 1) {
                    mnemonicChars[index++] = ' ';
                }
            }
            return seedCalculator.calculateSeed(mnemonicChars, passphrase);
        } finally {
            Arrays.fill(mnemonicChars, '\0');
            Arrays.fill(chars, null);
            for (final char[] charsToClear : toClear)
                Arrays.fill(charsToClear, '\0');
        }
    }
}
