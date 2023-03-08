package com.smallraw.chain.bitcoincore.script

enum class ScriptType(val value: Int) {
    P2PKH(1),     // pay to pubkey hash (aka pay to address)
    P2PK(2),      // pay to pubkey
    P2SH(3),      // pay to script hash
    P2WPKH(4),    // pay to witness pubkey hash
    P2WSH(5),     // pay to witness script hash
    P2SHWPKH(6),  // P2WPKH nested in P2SH
    P2SHWSH(7),  // P2WSH nested in P2SH
    NULL_DATA(8),
    UNKNOWN(0);

    companion object {
        fun fromValue(value: Int): ScriptType? {
            return values().find { it.value == value }
        }
    }

    val isWitness: Boolean
        get() = this in arrayOf(P2WPKH, P2WSH, P2SHWPKH, P2SHWSH)
}