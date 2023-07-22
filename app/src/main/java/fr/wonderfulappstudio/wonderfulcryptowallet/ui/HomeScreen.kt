package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.HomeUiState
import fr.wonderfulappstudio.wonderfulcryptowallet.HomeViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.R
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.composable.WonderfulTopBarWithActions
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.WonderfulCryptoWalletTheme
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.largeIconSize
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.largeSpacing
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.mediumIconSize
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.smallSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(true)
    )
    val coroutine = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = largeSpacing,
                        vertical = smallSpacing
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.add_wallet_title),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(smallSpacing))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.addWalletUiState.name,
                    onValueChange = viewModel::setWalletName,
                    label = {
                        Text(text = stringResource(R.string.add_wallet_name_label))
                    })
                Spacer(modifier = Modifier.size(smallSpacing))
                CryptoDropDownMenu(
                    cryptoSelected = viewModel.addWalletUiState.crypto,
                    onSelectCrypto = viewModel::setSelectedCrypto
                )
                Spacer(modifier = Modifier.size(smallSpacing))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.addWalletUiState.address,
                    onValueChange = viewModel::setNewAddress,
                    label = {
                        Text(text = stringResource(R.string.add_wallet_address_label))
                    })
                Spacer(modifier = Modifier.size(smallSpacing))
                Button(
                    onClick = {
                        viewModel.addNewWallet {
                            coroutine.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.add_wallet_button))
                }
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            WonderfulTopBarWithActions(title = stringResource(id = R.string.app_name)) {
                RefreshIconAnimation(isPlaying = viewModel.uiState is HomeUiState.Loading) {
                    viewModel.refresh()
                }
                IconButton(onClick = {
                    coroutine.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (viewModel.uiState) {
                HomeUiState.Loading -> {
                    print("Loading")
                }

                is HomeUiState.Success -> {
                    items((viewModel.uiState as HomeUiState.Success).walletData) { wallet ->

                        val dismissState = rememberDismissState()

                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {

                            viewModel.removeWallet(wallet)

                        }
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier
                                .padding(vertical = Dp(1f)),
                            directions = setOf(
                                DismissDirection.EndToStart
                            ),
                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.White
                                        else -> Color.Red
                                    }
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Default.Delete

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = Dp(20f)),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Delete Icon",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },
                            dismissContent = {
                                    WalletRow(
                                        wallet = wallet
                                    )
                            }
                        )

                        Divider(Modifier.fillMaxWidth(), color = Color.DarkGray)

                    }
                }
            }
        }
    }
}

@Composable
fun WalletRow(wallet: Wallet) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = wallet.crypto.icon),
                contentDescription = wallet.crypto.name,
                modifier = Modifier.size(
                    largeIconSize
                )
            )
            Column(modifier = Modifier.padding(smallSpacing)) {
                Text(text = wallet.name)
                Text(
                    text = "%f ${wallet.crypto.symbol}".format(wallet.balance),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "$%.2f".format(wallet.balance * wallet.crypto.currentPrice))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CryptoDropDownMenu(
    cryptoSelected: Crypto,
    onSelectCrypto: (Crypto) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = cryptoSelected.name,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Crypto.supportedCrypto.sortedBy { it != cryptoSelected }.forEach { crypto ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = crypto.icon),
                            contentDescription = crypto.name
                        )
                    },
                    onClick = {
                        onSelectCrypto(crypto)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = crypto.name,
                            modifier = Modifier.weight(1f)
                        )
                        if (cryptoSelected == crypto) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.size(mediumIconSize)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RefreshIconAnimation(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onClick: () -> Unit
) {
    // Allow resume on rotation
    var currentRotation by remember { mutableStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            // Infinite repeatable rotation when is playing
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = value
            }
        } else {
            if (currentRotation > 0f) {
                // Slow down rotation on pause
                rotation.animateTo(
                    targetValue = currentRotation + 50,
                    animationSpec = tween(
                        durationMillis = 1250,
                        easing = LinearOutSlowInEasing
                    )
                ) {
                    currentRotation = value
                }
            }
        }
    }
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.rotate(rotation.value)
        )
    }
}

@Preview
@Composable
fun PreviewWalletRow() {
    WonderfulCryptoWalletTheme {
        WalletRow(
            wallet = Wallet(
                "My wallet",
                "34xp4vRoCGJym3xR7yCVPFHoCNxv4Twseo",
                Crypto("Bitcoin", "BTC", R.drawable.bitcoin, 28136.82),
                0.001
            )
        )
    }
}
