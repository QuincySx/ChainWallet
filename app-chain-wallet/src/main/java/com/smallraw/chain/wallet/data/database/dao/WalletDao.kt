/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.chain.wallet.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.smallraw.chain.wallet.data.database.entity.AccountDO
import com.smallraw.chain.wallet.data.database.entity.WalletDO
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WalletDao : BaseDao<WalletDO> {
    @Query("SELECT COUNT(id) FROM wallet_table")
    abstract fun count(): Int

    @Query("SELECT COUNT(id) FROM wallet_table")
    abstract fun countStream(): Flow<Int>

    @Query("SELECT * FROM wallet_table")
    abstract fun getAll(): List<WalletDO>

    @Query("SELECT * FROM wallet_table JOIN account_table ON wallet_table.id = account_table.wallet_id")
    abstract fun getAllAndAccount(): Map<WalletDO, List<AccountDO>>

    @Query("SELECT * FROM wallet_table WHERE id = :id")
    abstract fun findById(id: Long): WalletDO
}
