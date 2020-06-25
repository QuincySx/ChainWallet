package com.smallraw.chain.wallet

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smallraw.authority.AuthorityKey
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val get = ViewModelProvider(application as App).get(AppViewModel::class.java)
        get.data.observe(this, Observer {
            btnText.text = it
        })

        btnText.setOnClickListener {
//            val sha1 = AuthorityKey.getSignaturesSha1(this)
//            Log.e("==sha1==", sha1)

            val check = AuthorityKey.checkValidity(this)
            Log.e("==check==", check.toString())

            val str = AuthorityKey.getAuthorityKey(this)
            Log.e("=====", str)
            get.data.value = "5678"
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }
}
