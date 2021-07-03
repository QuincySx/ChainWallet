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

import androidx.room.*
import java.util.*

@Entity(
    tableName = ConfigDO.TABLE_NAME,
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
class ConfigDO {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "value")
    var value: String? = null
    @ColumnInfo(name = "overTime")
    var overTime: Long? = null
    @ColumnInfo(name = "createTime")
    var createTime: Date? = null

    constructor() {}

    @Ignore
    constructor(name: String, value: String, overTime: Long?) {
        this.name = name
        this.value = value
        this.overTime = overTime
        this.createTime = Date()
    }

    @Ignore
    constructor(name: String, value: String) {
        this.name = name
        this.value = value
        this.overTime = null
        this.createTime = Date()
    }

    @Ignore
    constructor(id: Long?, name: String, value: String, overTime: Long?, createTime: Date) {
        this.id = id
        this.name = name
        this.value = value
        this.overTime = overTime
        this.createTime = createTime
    }

    override fun toString(): String {
        return "ConfigDO{" +
            "id=" + id +
            ", name='" + name + '\''.toString() +
            ", value='" + value + '\''.toString() +
            ", overTime=" + overTime +
            ", createTime=" + createTime +
            '}'.toString()
    }

    companion object {
        const val TABLE_NAME = "config_table"
    }
}
