package com.smallraw.chain.lib.bitcoin

import android.util.Log
import com.smallraw.chain.lib.Address
import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.OpCodes
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.extensions.toHex
import java.security.PublicKey
import java.util.*

enum class AddressType {
    P2PKH,  // Pay to public key hash
    P2SH,   // Pay to script hash
    WITNESS // Pay to witness hash
}

abstract class BitcoinAddress(
    val type: AddressType,
    val hashKey: ByteArray,
    val version: Byte
) : Address {

    val scriptType: ScriptType
        get() = when (type) {
            AddressType.P2PKH -> ScriptType.P2PKH
            AddressType.P2SH -> ScriptType.P2SH
            AddressType.WITNESS ->
                if (hashKey.size == 20) ScriptType.P2WPKH else ScriptType.P2WSH
        }

    open val lockingScript: ByteArray
        get() = when (type) {
            AddressType.P2PKH -> OpCodes.p2pkhStart + OpCodes.push(hashKey) + OpCodes.p2pkhEnd
            AddressType.P2SH -> OpCodes.p2pshStart + OpCodes.push(hashKey) + OpCodes.p2pshEnd
            else -> throw AddressFormatException("Unknown Address Type")
        }

    override fun getFormat(): String {
        return Base58.encode(getAddress())
    }

    override fun getAddress(): ByteArray {
        Log.e("Ripemd160", hashKey.toHex())
        val addressBytes = ByteArray(1 + hashKey.size + 4)
        //拼接测试网络或正式网络前缀
        addressBytes[0] = version

        System.arraycopy(hashKey, 0, addressBytes, 1, hashKey.size)
        //进行双 Sha256 运算
        val check: ByteArray =
            Sha256.doubleSha256(addressBytes, addressBytes.size - 4)

        //将双 Sha256 运算的结果前 4位 拼接到尾部
        System.arraycopy(check, 0, addressBytes, hashKey.size + 1, 4)

        Arrays.fill(check, 0.toByte())
        return addressBytes
    }
}