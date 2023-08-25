/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smallraw.convention.apps

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureNativeCMake(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {

        defaultConfig {
            externalNativeBuild {
                cmake {
                    version = "3.18.1"
                    // Sets optional flags for the C compiler.
                    cFlags += "-fvisibility=hidden -fvisibility-inlines-hidden -ffunction-sections -fdata-sections "
                    // Sets optional flags for the C++ compiler.
                    cppFlags += "-fvisibility=hidden -fvisibility-inlines-hidden -ffunction-sections -fdata-sections -std=c++11 "

                    // arguments += "-DANDROID_TOOLCHAIN=gcc "//设置使用gcc编译，cmake默认使用clang编译
                }
            }
        }

        externalNativeBuild {
            cmake {
                path = file("CMakeLists.txt")
            }
        }
    }
}
