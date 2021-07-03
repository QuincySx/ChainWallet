package com.smallraw.chain.wallet

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.smallraw.chain.wallet.repository.DataRepository
import com.smallraw.chain.wallet.repository.database.AppDatabase
import com.smallraw.lib.featureflag.RuntimeBehavior

class App : Application(), ViewModelStoreOwner {
    private var mAppViewModelStore: ViewModelStore? = null
    private val mHandler: Handler? = null
    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        RuntimeBehavior.initialize(this, BuildConfig.DEBUG)
        DataRepository.getInstance(AppDatabase.getInstance(this))
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

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore!!
    }

    enum class ChainType(private val type: Int, private val enable: Boolean) {
        ETH(1, true),
        BTC(2, true),
    }
}