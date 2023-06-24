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
import fr.wonderfulappstudio.wonderfulcryptowallet.R
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.composable.WonderfulTopBarWithActions
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.WonderfulCryptoWalletTheme
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.largeIconSize
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.smallSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
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
            item {
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
                    text = "%f ${wallet.crypto.symbol}".format(wallet.amount),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "$%f".format(wallet.amount * wallet.crypto.currentPrice))
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