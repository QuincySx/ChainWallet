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
package com.smallraw.chain.wallet.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.smallraw.chain.wallet.data.database.entity.embed.BaseEntity

@Entity(
    tableName = WalletDO.TABLE_NAME,
    indices = [
        Index(value = ["account_id", "chain_table_id", "address"], unique = true)
    ]
)
data class WalletDO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,

    @ColumnInfo(name = "account_id")
    var accountId: Long? = null,

    // chain 表的 id
    @ColumnInfo(name = "chain_table_id")
    var chainTableId: Long? = null,


    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "address")
    var address: String? = null,

    @ColumnInfo(name = "derived_path")
    var derivedPath: String = "",

    @ColumnInfo(name = "other_payload")
    var other: String,
) : BaseEntity(id) {

    companion object {
        const val TABLE_NAME = "wallet_table"
    }
}
