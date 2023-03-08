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
import androidx.annotation.VisibleForTesting
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Check whether a feature should be enabled or not. Based on the priority of the different providers and if said
 * provider explicitly defines a value for that feature, the value of the flag is returned.
 */
object RuntimeBehavior {

    @VisibleForTesting
    internal val providers = CopyOnWriteArrayList<FeatureFlagProvider>()

    fun initialize(context: Context, isDebugBuild: Boolean) {
        if (isDebugBuild) {
            val runtimeFeatureFlagProvider = RuntimeFeatureFlagProvider(context)
            addProvider(runtimeFeatureFlagProvider)
            addProvider(TestFeatureFlagProvider)
            if (runtimeFeatureFlagProvider.isFeatureEnabled(TestSetting.DEBUG_FIREBASE)) {
                addProvider(QiniuConfigFeatureFlagProvider(true))
            }
        } else {
            addProvider(StoreFeatureFlagProvider())
            addProvider(QiniuConfigFeatureFlagProvider(false))
        }
    }

    fun isFeatureEnabled(feature: Feature): Boolean {
        return providers.filter { it.hasFeature(feature) }
            .sortedBy(FeatureFlagProvider::priority)
            .firstOrNull()
            ?.isFeatureEnabled(feature)
            ?: feature.defaultValue
    }

    fun refreshFeatureFlags() {
        providers.filter { it is RemoteFeatureFlagProvider }.forEach { (it as RemoteFeatureFlagProvider).refreshFeatureFlags() }
    }

    fun addProvider(provider: FeatureFlagProvider) {
        providers.add(provider)
    }

    fun clearFeatureFlagProviders() = providers.clear()

    fun removeAllFeatureFlagProviders(priority: Int) = providers.removeAll(providers.filter { it.priority == priority })
}
