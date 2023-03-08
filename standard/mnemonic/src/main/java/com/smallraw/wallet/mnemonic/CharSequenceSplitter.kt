package com.smallraw.wallet.mnemonic

import java.util.*

internal class CharSequenceSplitter(private val separator1: Char, private val separator2: Char) {
    fun split(charSequence: CharSequence): List<CharSequence> {
        val list = LinkedList<CharSequence>()
        var start = 0
        val length = charSequence.length
        for (i in 0 until length) {
            val c = charSequence[i]
            if (c == separator1 || c == separator2) {
                list.add(charSequence.subSequence(start, i))
                start = i + 1
            }
        }
        list.add(charSequence.subSequence(start, length))
        return list
    }

    companion object{
        fun split(charSequence: CharSequence): List<CharSequence> {
            val list = LinkedList<CharSequence>()
            var start = 0
            val length = charSequence.length
            for (i in 0 until length) {
                val c = charSequence[i]
                if (c == ' ' ||
                    c == Normalization.normalizeNFKD(' ') ||
                    c == '\uFEFF' ||
                    c == '\u3000' ||
                    c == '\u2060' ||
                    c == '\u205F' ||
                    c == '\u202F' ||
                    c == '\u200D' ||
                    c == '\u200C' ||
                    c == '\u200B' ||
                    c == '\u200A' ||
                    c == '\u2009' ||
                    c == '\u2008' ||
                    c == '\u2007' ||
                    c == '\u2006' ||
                    c == '\u2005' ||
                    c == '\u2004' ||
                    c == '\u2003' ||
                    c == '\u2002' ||
                    c == '\u180E' ||
                    c == '\u1680' ||
                    c == '\u00A0' ||
                    c == '\u0020'
                ) {
                    list.add(charSequence.subSequence(start, i))
                    start = i + 1
                }
            }
            list.add(charSequence.subSequence(start, length))
            return list
        }
    }
}