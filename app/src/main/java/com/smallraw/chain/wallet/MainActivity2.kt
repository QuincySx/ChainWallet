package com.smallraw.chain.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smallraw.chain.wallet.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private val binding by lazy {
        ActivityMain2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val get = ViewModelProvider(application as App).get(AppViewModel::class.java)
        get.data.observe(this, Observer {
            binding.btnText.text = it
        })

        binding.btnText.setOnClickListener {
            get.data.value = "1234"
        }

        binding.btnCatch.setOnClickListener {
            throw RuntimeException("我炸了")
        }
    }
}