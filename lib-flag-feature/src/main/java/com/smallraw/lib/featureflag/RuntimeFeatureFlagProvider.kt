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

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting

class RuntimeFeatureFlagProvider : FeatureFlagProvider {

    private val preferences: SharedPreferences

    override val priority = MEDIUM_PRIORITY

    constructor(applicationContext: Context) {
        preferences = applicationContext.getSharedPreferences("runtime.featureflags", Context.MODE_PRIVATE)
    }

    @VisibleForTesting
    internal constructor(prefs: SharedPreferences) {
        preferences = prefs
    }

    override fun isFeatureEnabled(feature: Feature): Boolean =
        preferences.getBoolean(feature.key, feature.defaultValue)

    override fun hasFeature(feature: Feature): Boolean = true

    fun setFeatureEnabled(feature: Feature, enabled: Boolean) = preferences.edit().putBoolean(feature.key, enabled).apply()
}
