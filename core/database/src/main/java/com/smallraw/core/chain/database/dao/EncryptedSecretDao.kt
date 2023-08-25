package com.smallraw.core.chain.database.dao

import androidx.room.Dao
import com.smallraw.core.chain.database.model.EncryptedSecretEntity

@Dao
interface EncryptedSecretDao : BaseDao<EncryptedSecretEntity> {

}