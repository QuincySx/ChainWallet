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
    tableName = ConfigDO.TABLE_NAME,
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class ConfigDO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "value")
    var value: String,
) : BaseEntity(id){
    override fun toString(): String {
        return "ConfigDO{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", value='" + value + '\''.toString() +
                '}'.toString()
    }

    companion object {
        const val TABLE_NAME = "config_table"
    }
}
