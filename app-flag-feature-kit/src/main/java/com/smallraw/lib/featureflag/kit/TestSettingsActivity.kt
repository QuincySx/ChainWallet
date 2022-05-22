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
package com.smallraw.lib.featureflag.kit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private const val TAG_TEST_SETTING = "tag_testsettingfragment"

class TestSettingsActivity : AppCompatActivity(), TestSettingsFragment.TestSettingsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testsettings)

        if (supportFragmentManager.findFragmentByTag(TAG_TEST_SETTING) == null) {
            val settingsFragment = TestSettingsFragment().apply { testSettingListener = this@TestSettingsActivity }
            supportFragmentManager.beginTransaction()
                .replace(R.id.framelayout_testsettings_fragmentcontainer, settingsFragment).commit()
        } else {
            (supportFragmentManager.findFragmentByTag(TAG_TEST_SETTING) as TestSettingsFragment).testSettingListener =
                this
        }
    }

    override fun onFeatureToggleClicked() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_testsettings_fragmentcontainer,
                FeatureSelectFragment.getInstance(false)
            )
            .addToBackStack(null).commit()
    }

    override fun onTestSettingClicked() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_testsettings_fragmentcontainer,
                FeatureSelectFragment.getInstance(true)
            )
            .addToBackStack(null).commit()
    }
}
