package com.smallraw.chain.bitcoin.execptions

import java.lang.RuntimeException

class AddressFormatException(msg: String) : RuntimeException(msg)