package com.smallraw.wallet.mnemonic

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

enum class WordType(val path: String, val space: Char) {
    CHINESE_SIMPLIFIED("chinese_simplified.txt", ' '),
    CHINESE_TRADITIONAL("chinese_traditional.txt", ' '),
    CZECH("czech.txt", ' '),
    ENGLISH("english.txt", ' '),
    FRENCH("french.txt", ' '),
    ITALIAN("italian.txt", ' '),
    JAPANESE("japanese.txt", '\u3000'),
    KOREAN("korean.txt", ' '),
    SPANISH("spanish.txt", ' ');
}

class WordList(
    private val context: Context,
    private val wordType: WordType = WordType.ENGLISH,
    val supportFirst: Boolean = false
) {
    companion object {
        @JvmStatic
        private val wordListHashMap = SoftHashMap<WordType, ArrayList<String>?>()

        @JvmStatic
        private val wordFirstHashMap = SoftHashMap<WordType, HashMap<String, String>?>()
    }

    private fun loadWord(wordType: WordType) = synchronized(WordList::class.java) {
        val works = ArrayList<String>(2048)

        val worksFirst = if (supportFirst) {
            HashMap<String, String>(2048)
        } else null

        val open = BufferedReader(InputStreamReader(context.assets.open(wordType.path)))
        var str: String?
        do {
            str = open.readLine()
            str?.let {
                works.add(str)

                if (supportFirst) {
                    if (str.length >= 4) {
                        worksFirst!![str.substring(0, 4)] = str
                    } else {
                        worksFirst!![str] = str
                    }
                }

            }
        } while (str != null)
        wordListHashMap[wordType] = works

        if (supportFirst) {
            wordFirstHashMap[wordType] = worksFirst
        }

        return@synchronized works
    }

    fun getWordByWordFirst(word: CharSequence): String? {
        (wordFirstHashMap[wordType] ?: loadWord(wordType)).apply {
            val hashMap = wordFirstHashMap[wordType]!!
            return when (word.trim().length) {
                0, 1, 2, 3 -> null
                4 -> hashMap[word]
                else -> hashMap[word.substring(0, 4)]
            }
        }
    }

    /**
     * Get a word in the word list.
     *
     * @param index Index of word in the word list [0..2047] inclusive.
     * @return the word from the list.
     */
    fun getWord(index: Int): String {
        return wordListHashMap.get(wordType)?.get(index) ?: loadWord(wordType).get(index)
    }

    fun getIndex(word: String?): Int {
        val size: Int = wordListHashMap.get(wordType)?.size ?: loadWord(wordType).size
        val get = wordListHashMap.get(wordType)
        if (word == null) {
            for (i in 0 until size) if (get?.get(i) == null) return i
        } else {
            for (i in 0 until size) if (word == get?.get(i)) return i
        }
        return -1
    }

    /**
     * Get the space character for this language.
     *
     * @return a whitespace character.
     */
    fun getSpace(): Char {
        return wordType.space
    }
}