package fr.wonderfulappstudio.wonderfulcryptowallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomWallet(
    @PrimaryKey
    val name: String,
    val address: String,
    val cryptoSymbol: String,
    val balance: Double
)
