package fr.wonderfulappstudio.wonderfulcryptowallet.ui.model

import androidx.compose.runtime.Stable
import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.RoomWallet
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.Crypto

@Stable
data class Wallet(
    val name: String,
    val address: String,
    val crypto: Crypto,
    val balance: Double
) {

    fun toRoomWallet(): RoomWallet = RoomWallet(
        this.name,
        this.address,
        this.crypto.symbol,
        this.balance
    )

    companion object {
        fun fromRoomWallet(roomWallet: RoomWallet): Wallet =
            Wallet(
                roomWallet.name,
                roomWallet.address,
                Crypto.supportedCrypto.first { it.symbol == roomWallet.cryptoSymbol },
                roomWallet.balance
            )
    }
}