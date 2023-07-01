package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.compose.runtime.Stable

@Stable
data class AddWalletUiState(
    val name: String = "",
    val crypto: Crypto = Crypto.supportedCrypto[0],
    val address: String = ""
)
