package com.smallraw.authority

import android.content.Context

class AuthorityKey {
    companion object {
        init {
            System.loadLibrary("dexinterpret")
        }
    }

    external fun checkValidity(context: Context?): Boolean
    external fun getAuthorityKey(context: Context?): String?
}