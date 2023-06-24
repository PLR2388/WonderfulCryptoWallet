package fr.wonderfulappstudio.wonderfulcryptowallet.ui

import androidx.annotation.DrawableRes

data class Crypto(
    val name: String,
    val symbol: String,
    @DrawableRes val icon: Int,
    val currentPrice: Double
)