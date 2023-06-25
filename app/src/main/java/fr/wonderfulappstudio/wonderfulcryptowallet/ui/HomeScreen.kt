package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.HomeUiState
import fr.wonderfulappstudio.wonderfulcryptowallet.HomeViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.R
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.composable.WonderfulTopBarWithActions
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.WonderfulCryptoWalletTheme
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.largeIconSize
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.smallSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        WonderfulTopBarWithActions(title = stringResource(id = R.string.app_name)) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when(viewModel.uiState) {
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