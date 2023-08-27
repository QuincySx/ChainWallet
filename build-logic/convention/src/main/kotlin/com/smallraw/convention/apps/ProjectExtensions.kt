package com.smallraw.convention.apps

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

val appCompileSdk
    get(): Int = 34

val appBuildToolsVersion
    get(): String = "34.0.0"

val appTargetSdk
    get(): Int = 33

val appMinSdk
    get(): Int = 21