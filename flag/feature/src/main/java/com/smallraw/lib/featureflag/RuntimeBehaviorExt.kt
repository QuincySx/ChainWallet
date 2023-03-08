package com.smallraw.lib.featureflag

fun Feature.isEnable(): Boolean {
    return RuntimeBehavior.isFeatureEnabled(this)
}