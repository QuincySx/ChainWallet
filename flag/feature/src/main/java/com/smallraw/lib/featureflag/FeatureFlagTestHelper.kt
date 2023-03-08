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

/**
 * Must be used only from test source code.
 */
class FeatureFlagTestHelper {

    companion object {
        @JvmStatic
        fun enableFeatureFlag(feature: Feature) {
            RuntimeBehavior.addProvider(TestFeatureFlagProvider(feature))
        }

        @JvmStatic
        fun disableFeatureFlag(feature: Feature) {
            RuntimeBehavior.addProvider(TestFeatureFlagProvider(feature, false))
        }

        @JvmStatic
        fun clearFeatureFlags() {
            RuntimeBehavior.removeAllFeatureFlagProviders(TEST_PRIORITY)
        }

        const val TEST_PRIORITY = MAX_PRIORITY - 1 // preceeds everyone
    }

    private class TestFeatureFlagProvider(val feature: Feature, val enabled: Boolean = true) :
        FeatureFlagProvider {
        override val priority: Int = TEST_PRIORITY

        override fun isFeatureEnabled(feature: Feature) =
            if (feature == this.feature) enabled else false

        override fun hasFeature(feature: Feature) = (feature == this.feature)
    }
}
