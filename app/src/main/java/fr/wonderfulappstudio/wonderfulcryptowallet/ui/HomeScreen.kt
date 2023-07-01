package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.add_wallet_button))
                }
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            WonderfulTopBarWithActions(title = stringResource(id = R.string.app_name)) {
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
                    items((viewModel.uiState as HomeUiState.Success).walletData.wallets) { wallet ->
                        WalletRow(
                            wallet = wallet
                        )
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
            Text(text = "$%f".format(wallet.balance * wallet.crypto.currentPrice))
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