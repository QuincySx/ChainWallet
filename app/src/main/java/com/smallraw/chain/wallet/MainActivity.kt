package com.smallraw.chain.wallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smallraw.authority.AuthorityKey
import com.smallraw.chain.wallet.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val get = ViewModelProvider(application as App).get(AppViewModel::class.java)
        get.data.observe(this, Observer {
            binding.btnText.text = it
        })

        binding.btnText.setOnClickListener {
//            val sha1 = AuthorityKey.getSignaturesSha1(this)
//            Log.e("==sha1==", sha1)

            val authorityKey = AuthorityKey()
            val check = authorityKey.checkValidity(this)
            Log.e("==check==", check.toString())

            val str = authorityKey.getAuthorityKey(this)
            Log.e("=====", str ?: "")
            get.data.value = "5678"
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }
}
