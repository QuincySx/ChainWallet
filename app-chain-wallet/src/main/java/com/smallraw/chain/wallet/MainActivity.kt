package com.smallraw.chain.wallet

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.smallraw.chain.wallet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //通过actionbardrawertoggle将toolbar与drawablelayout关联起来
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.app_name, R.string.app_name
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // 主页内容
                val contentView: View = binding.drawerLayout.getChildAt(0)
                // 侧边栏
                // slideOffset 值默认是0~1
                contentView.translationX = drawerView.measuredWidth * slideOffset
            }
        }

        toggle.syncState()
        binding.drawerLayout.addDrawerListener(toggle)

    }
}
