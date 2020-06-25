package com.smallraw.chain.wallet;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;


public class App extends Application implements ViewModelStoreOwner {
    private ViewModelStore mAppViewModelStore;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mAppViewModelStore = new ViewModelStore();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mAppViewModelStore;
    }
}
