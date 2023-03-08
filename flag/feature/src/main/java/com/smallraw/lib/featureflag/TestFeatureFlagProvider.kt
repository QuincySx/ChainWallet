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
 * For use during unit/instrumentation tests, allows to dynamically enable/disable features
 * during automated tests
 */
object TestFeatureFlagProvider : FeatureFlagProvider {

    private val features = HashMap<Feature, Boolean>()

    override val priority = TEST_PRIORITY

    override fun isFeatureEnabled(feature: Feature): Boolean = features[feature]!!

    override fun hasFeature(feature: Feature): Boolean = features.containsKey(feature)

    fun setFeatureEnabled(feature: Feature, enabled: Boolean) = features.put(feature, enabled)

    fun clearFeatures() = features.clear()
}
