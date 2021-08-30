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
import com.smallraw.chain.wallet.data.database.entity.ChainDO

@Dao
abstract class ChainDao : BaseDao<ChainDO> {
    @Query("SELECT COUNT(*) FROM chain_table")
    abstract fun count(): Int

    @Query("SELECT * FROM chain_table WHERE id = :id")
    abstract fun findById(id: Long): ChainDO
}
