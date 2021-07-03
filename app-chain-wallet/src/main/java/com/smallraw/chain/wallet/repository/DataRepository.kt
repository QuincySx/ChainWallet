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
package com.smallraw.chain.wallet.repository

import com.smallraw.chain.wallet.repository.database.AppDatabase

class DataRepository(database: AppDatabase) {
    private val mDatabase: AppDatabase = database

    companion object {
        @JvmStatic
        @Volatile
        private var sInstance: DataRepository? = null

        @JvmStatic
        fun getInstance(database: AppDatabase): DataRepository {
            return sInstance ?: synchronized(DataRepository::class) {
                sInstance ?: DataRepository(database).also { sInstance = it }
            }
        }
    }
}
