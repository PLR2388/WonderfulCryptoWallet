package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.compose.runtime.Stable

@Stable
data class Wallet(
    val name: String,
    val address: String,
    val crypto: Crypto,
    val amount: Double
)
