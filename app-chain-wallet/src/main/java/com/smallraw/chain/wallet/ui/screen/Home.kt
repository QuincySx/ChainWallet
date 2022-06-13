package com.smallraw.chain.wallet.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.smallraw.chain.wallet.ui.components.PanelSurface
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState

@Composable
fun Home() {
    val panelsState = rememberOverlappingPanelsState()
    val gesturesEnabled by remember { mutableStateOf(true) }

    OverlappingPanels(
        modifier = Modifier.fillMaxSize(),
        panelsState = panelsState,
        gesturesEnabled = gesturesEnabled,
        panelStart = {
            AccountManager()
        },
        panelCenter = {
            PanelSurface {
                Text(text = "B")
            }
        },
        panelEnd = {
            PanelSurface {
                Text(text = "C")
            }
        },
    )
}