package com.smallraw.chain.wallet.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.metrics.performance.PerformanceMetricsState
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberMetricsStateHolder(): PerformanceMetricsState.Holder {
    val localView = LocalView.current

    return remember(localView) {
        PerformanceMetricsState.getHolderForHierarchy(localView)
    }
}

@Composable
fun JankMetricEffect(
    vararg keys: Any?,
    reportMetric: suspend CoroutineScope.(state: PerformanceMetricsState.Holder) -> Unit
) {
    val metrics = rememberMetricsStateHolder()
    LaunchedEffect(metrics, *keys) {
        reportMetric(metrics)
    }
}

@Composable
fun JankMetricDisposableEffect(
    vararg keys: Any?,
    reportMetric: DisposableEffectScope.(state: PerformanceMetricsState.Holder) -> DisposableEffectResult
) {
    val metrics = rememberMetricsStateHolder()
    DisposableEffect(metrics, *keys) {
        reportMetric(this, metrics)
    }
}

//@OptIn(InternalCoroutinesApi::class)
//@Composable
//fun TrackScrollJank(scrollableState: ScrollableState, stateName: String) {
//    JankMetricEffect(scrollableState) { metricsHolder ->
//        snapshotFlow { scrollableState.isScrollInProgress }.collect { isScrollInProgress ->
//            metricsHolder.state?.apply {
//                if (isScrollInProgress) {
//                    putState(stateName, "Scrolling=true")
//                } else {
//                    removeState(stateName)
//                }
//            }
//        }
//    }
//}
