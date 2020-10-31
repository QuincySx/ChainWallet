package com.smallraw.wallet.mnemonic

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.SoftReference

enum class WordType(val path: String) {
    CHINESE_SIMPLIFIED("chinese_simplified.txt"),
    CHINESE_TRADITIONAL("chinese_traditional.txt"),
    CZECH("czech.txt"),
    ENGLISH("english.txt"),
    FRENCH("french.txt"),
    ITALIAN("italian.txt"),
    JAPANESE("japanese.txt"),
    KOREAN("korean.txt"),
    SPANISH("spanish.txt");
}

class WordList(private val context: Context, private val wordType: WordType = WordType.ENGLISH) {
    private var wordList = SoftReference<ArrayList<String>?>(null)

    private fun loadWord() = synchronized(this) {
        val works = ArrayList<String>(2048)
        val open = BufferedReader(InputStreamReader(context.assets.open(wordType.path)))
        var str: String?
        do {
            str = open.readLine()
            str?.let {
                works.add(str)
            }
        } while (str != null)
        wordList = SoftReference<ArrayList<String>?>(works)
        return@synchronized works
    }

    /**
     * Get a word in the word list.
     *
     * @param index Index of word in the word list [0..2047] inclusive.
     * @return the word from the list.
     */
    fun getWord(index: Int): String {
        return wordList.get()?.get(index) ?: loadWord().get(index)
    }

    fun getIndex(word: String?): Int {
        val size: Int = wordList.get()?.size ?: loadWord().size
        val get = wordList.get()
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
        return ' '
    }
}