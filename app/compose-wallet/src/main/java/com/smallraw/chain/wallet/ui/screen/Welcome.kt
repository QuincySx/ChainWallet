package com.smallraw.chain.wallet.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smallraw.chain.wallet.designsystem.component.BackgroundSurface
import com.smallraw.chain.wallet.ui.navigation.Screens
import com.smallraw.chain.wallet.ui.screen.app.AppState

@Composable
fun WelcomeScreen(
    appState: AppState,
) {
    BackgroundSurface {
        Box(contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    appState.navigate(Screens.CreateWallet)
                }) {
                    Text("创建")
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                    Text("恢复")
                }
            }
        }
    }
}
