package com.smallraw.chain.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smallraw.chain.wallet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}
