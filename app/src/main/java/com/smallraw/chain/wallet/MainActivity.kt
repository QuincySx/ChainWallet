package com.smallraw.chain.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val get = ViewModelProvider(application as App).get(AppViewModel::class.java)
        get.data.observe(this, Observer {
            btnText.text = it
        })

        btnText.setOnClickListener {
            get.data.value = "5678"
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }
}
