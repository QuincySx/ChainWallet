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

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = AccountDO.TABLE_NAME,
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
class AccountDO {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "encrypted")
    var encrypted: String? = null

    @ColumnInfo(name = "account_type")
    @AccountType
    var accountType: Int = AccountType.MNEMONIC

    @ColumnInfo(name = "overTime")
    var overTime: Long? = null

    @ColumnInfo(name = "createTime")
    var createTime: Date? = null

    constructor() {}

    @IntDef(
        AccountType.MNEMONIC,
        AccountType.PRIVATE,
        AccountType.IMPORT_MNEMONIC,
        AccountType.IMPORT_PRIVATE,
        AccountType.IMPORT_KEYSTORE,
        AccountType.OBSERVE,
    )
    annotation class AccountType {
        companion object {
            const val MNEMONIC: Int = 0
            const val PRIVATE: Int = 1
            const val IMPORT_MNEMONIC: Int = 2
            const val IMPORT_PRIVATE: Int = 3
            const val IMPORT_KEYSTORE: Int = 4
            const val OBSERVE: Int = 5
        }
    }

    companion object {
        const val TABLE_NAME = "account_table"
    }

    override fun toString(): String {
        return "AccountDO(id=$id, name=$name, encrypted=$encrypted, accountType=$accountType, overTime=$overTime, createTime=$createTime)"
    }
}


