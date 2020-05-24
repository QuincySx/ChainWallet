package com.smallraw.chain.lib.execptions

import java.lang.IllegalArgumentException
import java.lang.RuntimeException

class PrivateKeyException : RuntimeException() {
    class AbnormalLength() : IllegalArgumentException()
}