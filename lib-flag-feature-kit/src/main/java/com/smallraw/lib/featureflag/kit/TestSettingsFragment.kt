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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TestSettingsFragment : androidx.fragment.app.Fragment() {

    interface TestSettingsListener {
        fun onFeatureToggleClicked()
        fun onTestSettingClicked()
    }

    var testSettingListener: TestSettingsListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_testsettings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.textview_testsettings_explanation)
            .setOnClickListener { testSettingListener?.onFeatureToggleClicked() }

        view.findViewById<View>(R.id.formattextview_testsettings_testsetting)
            .setOnClickListener { testSettingListener?.onTestSettingClicked() }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "Test Settings"
    }
}
