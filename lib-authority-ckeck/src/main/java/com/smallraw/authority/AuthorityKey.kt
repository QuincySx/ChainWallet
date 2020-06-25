package com.smallraw.authority

import android.content.Context

class AuthorityKey {
    companion object {
        external fun checkValidity(context: Context?): Boolean
        external fun getAuthorityKey(context: Context?): String?

        init {
            System.loadLibrary("dexinterpret")
        }
    }
}