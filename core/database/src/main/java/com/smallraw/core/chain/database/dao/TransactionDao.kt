package com.smallraw.core.chain.database.dao

import androidx.room.Dao
import com.smallraw.core.chain.database.model.TransactionEntity

@Dao
interface TransactionDao : BaseDao<TransactionEntity> {

}