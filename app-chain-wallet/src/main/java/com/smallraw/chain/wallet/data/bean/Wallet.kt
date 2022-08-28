package com.smallraw.chain.wallet.data.bean

import com.smallraw.chain.wallet.data.database.entity.WalletDO
import java.util.*

data class Wallet(
    val id: Long? = null,
    var name: String,
    var isBackup: Boolean,
    private var encrypted: String,
    @WalletDO.Type
    var type: Int = WalletDO.Type.MNEMONIC,
    @WalletDO.SourceType
    var sourceType: Int = WalletDO.SourceType.CREATE,
    private var createTime: Date? = null,
) {
    companion object {
        fun from(walletDO: WalletDO): Wallet {
            return Wallet(
                walletDO.id,
                walletDO.name,
                walletDO.isBackup,
                walletDO.encrypted,
                walletDO.type,
                walletDO.sourceType,
                walletDO.createdAt
            )
        }
    }
}
