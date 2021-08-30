package com.smallraw.chain.wallet.data.database.entity.embed

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.smallraw.time.database.converter.DateConverter
import java.util.*


open class BaseEntity(
    open val id: Long? = null,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(DateConverter::class)
    var createdAt: Date? = null,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(DateConverter::class)
    var updatedAt: Date? = null
) {
    constructor() : this(null)
}