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
package com.smallraw.chain.wallet.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smallraw.chain.wallet.BuildConfig
import com.smallraw.chain.wallet.repository.database.converter.ChainTypeConverter
import com.smallraw.chain.wallet.repository.database.converter.StringArrayConverter
import com.smallraw.chain.wallet.repository.database.dao.*
import com.smallraw.chain.wallet.repository.database.entity.*
import com.smallraw.time.database.converter.DateConverter

@Database(
    entities = [
        ConfigDO::class,
        AccountDO::class,
        ChainRpcURLDO::class,

        ChainBtcDO::class,
        WalletBtcDO::class,

        ChainEthDO::class,
        WalletEthDO::class,
        TokenEthDO::class,
        WalletTokenEthDO::class,
    ],
    version = AppDatabase.DATABASE_LAST_VERSION,
    exportSchema = false
)
@TypeConverters(DateConverter::class, ChainTypeConverter::class, StringArrayConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun chainBtcDao(): ChainBtcDao
    abstract fun chainEthDao(): ChainEthDao
    abstract fun walletBtcDao(): WalletBtcDao
    abstract fun walletEthDao(): WalletEthDao

    abstract fun configDao(): ConfigDao

    companion object {
        const val DATABASE_NAME = "wallet_db_v1"
        const val DATABASE_LAST_VERSION = 1

        @Volatile
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return sInstance ?: synchronized(this) {
                sInstance ?: buildDatabase(context).also { sInstance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .apply {
                    if (!BuildConfig.DEBUG) fallbackToDestructiveMigration()
                }
                .build()
        }
    }
}
