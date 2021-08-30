package com.smallraw.chain.wallet.data.database.entity.embed

import androidx.room.ColumnInfo

data class DigitalAssetUnitDO(
    @ColumnInfo(name = "currency_name")
    val currencyName: String = "",

    @ColumnInfo(name = "currency_symbol")
    val currencySymbol: String = "",

    @ColumnInfo(name = "currency_decimals")
    val currencyDecimals: Int = 0,
)