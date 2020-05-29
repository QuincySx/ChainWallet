package com.smallraw.chain.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val get = ViewModelProvider(application as App).get(AppViewModel::class.java)
        get.data.observe(this, Observer {
            btnText.text = it
        })

        btnText.setOnClickListener {
            get.data.value = "1234"
        }
    }
}