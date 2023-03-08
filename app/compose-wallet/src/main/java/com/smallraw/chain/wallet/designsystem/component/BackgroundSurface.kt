package com.smallraw.chain.wallet.designsystem.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smallraw.chain.wallet.designsystem.theme.LocalBackgroundTheme

@Composable
fun BackgroundSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize(),
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}