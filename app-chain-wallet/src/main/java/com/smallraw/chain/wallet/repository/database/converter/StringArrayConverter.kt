package com.smallraw.chain.wallet.repository.database.converter

import androidx.room.TypeConverter
import org.json.JSONArray
import org.json.JSONException

class StringArrayConverter {
    @TypeConverter
    fun toString(array: Collection<String>?): String {
        return JSONArray().apply {
            array?.forEach {
                put(it)
            }
        }.toString()
    }

    @TypeConverter
    fun toArray(str: String?): List<String> {
        try {
            JSONArray(str).also {
                val arrayList = ArrayList<String>(it.length())
                for (index in 0..it.length()) {
                    arrayList.add(it.getString(index))
                }
                return arrayList
            }
        } catch (e: JSONException) {
            return emptyList()
        }
    }
}