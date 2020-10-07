package com.smallraw.chain.bitcoincore.execptions

import java.lang.RuntimeException

open class BitcoinFormatException(msg: String) : RuntimeException(msg) {
    class AddressFormatException(msg: String) : BitcoinFormatException(msg)
    class AddressInitException(msg: String) : BitcoinFormatException(msg)
    class PrivateKeyWrongLengthException() : BitcoinFormatException("")
}

