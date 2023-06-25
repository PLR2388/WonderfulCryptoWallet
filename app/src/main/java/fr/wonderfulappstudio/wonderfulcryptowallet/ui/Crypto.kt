package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.annotation.DrawableRes
import fr.wonderfulappstudio.wonderfulcryptowallet.R

data class Crypto(
    val name: String,
    val symbol: String,
    @DrawableRes val icon: Int,
    val currentPrice: Double
) {
    companion object {
        private val bitcoin: Crypto = Crypto(
            "Bitcoin",
            "BTC",
            R.drawable.bitcoin,
            0.0
        )

        val supportedCrypto: List<Crypto> = listOf(bitcoin)
    }
}