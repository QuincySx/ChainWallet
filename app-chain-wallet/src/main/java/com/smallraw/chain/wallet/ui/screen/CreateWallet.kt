package com.smallraw.chain.wallet.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.smallraw.chain.wallet.ui.components.PanelSurface

@Composable
fun CreateWalletScreen(navCtrl: NavHostController){


    PanelSurface {
        Box(contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                    Text("创建钱包")
                }
            }
        }
    }
}