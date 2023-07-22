package fr.wonderfulappstudio.wonderfulcryptowallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomCrypto(
    @PrimaryKey
    val symbol: String,
    val currentPrice: Double
)
