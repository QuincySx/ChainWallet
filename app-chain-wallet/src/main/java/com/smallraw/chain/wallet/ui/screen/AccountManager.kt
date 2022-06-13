package com.smallraw.chain.wallet.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smallraw.chain.wallet.R
import com.smallraw.chain.wallet.data.bean.Account
import com.smallraw.chain.wallet.data.bean.IChain
import com.smallraw.chain.wallet.data.bean.IWallet
import com.smallraw.chain.wallet.ui.components.AsyncImage
import com.smallraw.chain.wallet.ui.components.PanelSurface
import com.smallraw.chain.wallet.ui.kit.WidgetAccountListItem
import com.smallraw.chain.wallet.ui.kit.WidgetChainCategory
import com.smallraw.chain.wallet.ui.kit.WidgetWalletContentVector
import com.smallraw.chain.wallet.ui.kit.WidgetWalletListItem

@Composable
fun AccountManager(modifier: Modifier = Modifier) {
    PanelSurface {
        Column(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
            ) {
                WalletList(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    wallets = arrayListOf()
                )
                AccountList(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(3.5f),
                    accounts = arrayListOf()
                )
            }
            CurrentAccountItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
            )
        }
    }
}

@Composable
private fun WalletList(
    modifier: Modifier = Modifier, wallets: List<IWallet>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            WidgetWalletListItem(modifier = Modifier.fillParentMaxWidth(),
                selected = false,
                showIndicator = false,
                onClick = {}) {
                WidgetWalletContentVector() {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_gradle_logo),
                        contentDescription = "Home",
                    )
                }
            }
        }

        item {
            Divider(
                modifier = Modifier
                    .fillParentMaxWidth(0.55f)
                    .padding(bottom = 4.dp)
                    .clip(MaterialTheme.shapes.medium),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outline,
            )
        }

        items(wallets) { wallet ->
            WidgetWalletListItem(selected = false, showIndicator = true, onClick = {}) {
                WidgetWalletContentVector {
                    Text(wallet.getChain().getName())
                }
            }
        }
    }
}


@Composable
private fun AccountList(
    modifier: Modifier = Modifier, accounts: List<Account>
) {
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
        ) {
            WalletListLoaded(
                modifier = Modifier.fillMaxSize(),
                bannerUrl = "",
                guildName = "",
                accountCategorys = mapOf()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentAccountItem(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp
    ) {
        Box(modifier = modifier) {
            Row(
                modifier = Modifier
                    .padding(
                        start = 12.dp, top = 12.dp, bottom = 12.dp, end = 4.dp
                    )
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.size(40.dp)) {
                    // avatar
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.titleSmall) {
                            // username
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun WalletListLoaded(
    bannerUrl: String?,
    guildName: String,
    accountCategorys: Map<IChain, List<Account>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                if (bannerUrl != null) {
                    AsyncImage(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .height(150.dp),
                        url = bannerUrl,
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillParentMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.DarkGray,
                                        Color.Transparent
                                    ),
                                ),
                                alpha = 0.8f
                            )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = guildName,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(0f, 5f),
                                blurRadius = 3f,
                            )
                        ),
                    )
                }
            }
        }
        for ((chain, accounts) in accountCategorys) {
            if (chain != null) {
                item {
                    val iconRotation by animateFloatAsState(
//                        targetValue = if (collapsed) -90f else 0f,
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    WidgetChainCategory(
                        modifier = Modifier.padding(
                            top = 12.dp,
                            bottom = 4.dp
                        ),
                        title = { Text(chain.getName()) },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_gradle_logo),
                                contentDescription = "Collapse category",
                                modifier = Modifier.rotate(iconRotation)
                            )
                        },
                        onClick = {
                        },
                    )
                }
            }

            items(accounts) { account ->
                WidgetAccountListItem(
                    modifier = Modifier.padding(bottom = 2.dp),
                    title = { Text(account.name) },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_gradle_logo),
                            contentDescription = null
                        )
                    },
                    selected = false,
                    showIndicator = false,
                    onClick = { /*TODO*/ },
                )
            }
        }
    }
}