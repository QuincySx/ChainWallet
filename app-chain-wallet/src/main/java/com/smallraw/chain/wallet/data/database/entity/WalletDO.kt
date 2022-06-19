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

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.smallraw.chain.wallet.data.database.entity.embed.BaseEntity

@Entity(
    tableName = WalletDO.TABLE_NAME,
    indices = [
        Index(value = ["encrypted"], unique = true)
    ]
)
data class WalletDO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "is_backup")
    var isBackup: Boolean,

    @ColumnInfo(name = "encrypted")
    var encrypted: String,

    @ColumnInfo(name = "type")
    @Type
    var type: Int,

    @ColumnInfo(name = "source_type")
    @SourceType
    var sourceType: Int,

    @ColumnInfo(name = "other_payload")
    var other: String,
) : BaseEntity(id) {

    @IntDef(
        Type.MNEMONIC,
        Type.PRIVATE,
        Type.KEYSTORE,
        Type.WATCHING,
    )
    annotation class Type {
        companion object {
            const val MNEMONIC: Int = 0
            const val PRIVATE: Int = 1
            const val KEYSTORE: Int = 2
            const val WATCHING: Int = 3
        }
    }

    @IntDef(
        SourceType.CREATE,
        SourceType.IMPORT
    )
    annotation class SourceType {
        companion object {
            const val CREATE: Int = 0
            const val IMPORT: Int = 1
        }
    }

    companion object {
        const val TABLE_NAME = "wallet_table"
    }

    override fun toString(): String {
        return "WalletDO(id=$id, name='$name', encrypted='$encrypted', type=$type, sourceType=$sourceType, other='$other')"
    }
}
