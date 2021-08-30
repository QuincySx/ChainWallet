package com.smallraw.chain.wallet.data.database.converter

import androidx.room.TypeConverter
import com.smallraw.chain.wallet.App

class ChainTypeConverter {
    @TypeConverter
    fun toChainType(type: Int?): App.ChainType {
        return App.ChainType.values().find { it.ordinal == type } ?: App.ChainType.ETH
    }

    @TypeConverter
    fun toInt(date: App.ChainType?): Int {
        return date?.ordinal ?: App.ChainType.ETH.ordinal
    }
}