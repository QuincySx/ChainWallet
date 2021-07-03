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
package com.smallraw.chain.wallet.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.smallraw.chain.wallet.App
import java.util.*

@Entity(
    tableName = ChainBtcDO.TABLE_NAME,
    indices = [
        Index(value = ["bip_44_index"], unique = true)
    ]
)
class ChainBtcDO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "chain_type")
    var chainType: App.ChainType = App.ChainType.BTC,

    @ColumnInfo(name = "bip_44_index")
    var bip44Index: Int = 0,

    @ColumnInfo(name = "currency_name")
    val currencyName: String = "",

    @ColumnInfo(name = "currency_symbol")
    val currencySymbol: String = "",

    @ColumnInfo(name = "currency_decimals")
    val currencyDecimals: Int = 0,

    @ColumnInfo(name = "create_time")
    var createTime: Date? = null,
) {
    companion object {
        const val TABLE_NAME = "chain_btc_table"
    }
}
