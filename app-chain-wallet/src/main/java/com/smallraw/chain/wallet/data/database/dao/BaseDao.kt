package com.smallraw.chain.wallet.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.smallraw.chain.wallet.data.database.entity.embed.BaseEntity
import java.util.*

abstract interface BaseDao<T : BaseEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(vararg obj: T)

    @Delete
    abstract fun delete(vararg obj: T)

    fun insertWithTimestamp(data: T) {
        insert(data.apply {
            createdAt = Date()
            updatedAt = Date()
        })
    }

    fun updateWithTimestamp(data: T) {
        update(data.apply {
            updatedAt = Date()
        })
    }
}