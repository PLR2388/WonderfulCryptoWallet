package fr.wonderfulappstudio.wonderfulcryptowallet.ui.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.wonderfulappstudio.wonderfulcryptowallet.R
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.theme.WonderfulCryptoWalletTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WonderfulTopBar(title: String) {
    TopAppBar(title = {
        Text(title)
    })
}

@Preview
@Composable
fun PreviewWonderfulTopBar() {
    WonderfulCryptoWalletTheme {
        WonderfulTopBar(stringResource(id = R.string.app_name))
    }
}