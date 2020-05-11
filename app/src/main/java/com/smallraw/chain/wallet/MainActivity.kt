package com.smallraw.chain.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smallraw.chain.lib.Testss

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Testss.test()
    }
}
