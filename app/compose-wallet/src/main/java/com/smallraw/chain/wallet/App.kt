package com.smallraw.chain.wallet

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.smallraw.lib.featureflag.RuntimeBehavior
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        RuntimeBehavior.initialize(this, BuildConfig.DEBUG)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    enum class ChainType(private val type: Int, private val enable: Boolean) {
        ETH(1, true),
        BTC(2, true),
    }
}