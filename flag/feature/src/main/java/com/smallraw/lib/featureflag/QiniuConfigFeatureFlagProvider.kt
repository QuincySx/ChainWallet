/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.lib.featureflag

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.read
import kotlin.concurrent.write

class QiniuConfigFeatureFlagProvider(
    private val isDevModeEnabled: Boolean,
    private val cacheExpirationSecs: Long = CACHE_EXPIRATION_SECS
) :
    FeatureFlagProvider,
    RemoteFeatureFlagProvider {

    private val url_qiniu_dev = "https://chainwallet.smallraw.com/config-debug.json"
    private val url_qiniu = "https://chainwallet.smallraw.com/config.json"

    private var jsonObjectData: JSONObject? = null
    private val rwlock = ReentrantReadWriteLock();

    init {
        refreshFeatureFlags()
    }

    private fun setData(message: String) = rwlock.write {
        try {
            jsonObjectData = JSONObject(message)
        } catch (e: Exception) {
        }
    }

    private fun getData(key: String): Boolean = rwlock.read {
        try {
            jsonObjectData?.getBoolean(key) ?: false
        } catch (e: Exception) {
            false
        }
    }

    override val priority: Int = MAX_PRIORITY

    override fun isFeatureEnabled(feature: Feature): Boolean =
        getData(feature.key)

    override fun hasFeature(feature: Feature): Boolean {
        return when (feature) {
            FeatureFlag.DARK_MODE -> true
            else -> false
        }
    }

    override fun refreshFeatureFlags() {
        HttpThread(
            if (isDevModeEnabled) {
                url_qiniu_dev
            } else {
                url_qiniu
            }
        ).start()
    }

    private fun getCacheExpirationSeconds(isDevModeEnabled: Boolean): Long = if (isDevModeEnabled) {
        CACHE_EXPIRATION_SECS_DEV
    } else {
        CACHE_EXPIRATION_SECS
    }

    inner class HttpThread(var baseUrl: String) : Thread("config-synchronous—thread") {
        override fun run() {
            super.run()
            val url = URL(baseUrl)
            val httpConnect = url.openConnection() as HttpsURLConnection

            httpConnect.connectTimeout = 5 * 1000  // 设置连接超时时间
            httpConnect.readTimeout = 5 * 1000  //设置从主机读取数据超时
            httpConnect.doOutput = true
            httpConnect.doInput = true
            httpConnect.useCaches = false
            httpConnect.connect() // 开始连接

            val inputStream = httpConnect.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val strBuilder = StringBuilder()
            reader.forEachLine {
                strBuilder.append(it)
            }

            setData(strBuilder.toString())

            inputStream.close()
            reader.close()
        }
    }

    companion object {
        const val CACHE_EXPIRATION_SECS = 1 * 60 * 60L
        const val CACHE_EXPIRATION_SECS_DEV = 1L
    }
}
