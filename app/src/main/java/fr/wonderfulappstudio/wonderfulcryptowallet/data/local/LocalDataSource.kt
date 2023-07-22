package fr.wonderfulappstudio.wonderfulcryptowallet.data.local

import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val appDatabase: AppDatabase
) {

    suspend fun getAllWallets(): List<Wallet> {
        return withContext(Dispatchers.IO) {
            val roomWallets = appDatabase.walletDao().getAll()
            val roomCrypto = appDatabase.cryptoDao().getAll()
            roomWallets.map { roomWallet ->
                val initialWallet = Wallet.fromRoomWallet(roomWallet)
                initialWallet.copy(crypto = initialWallet.crypto.copy(currentPrice = roomCrypto.first { it.symbol == initialWallet.crypto.symbol }.currentPrice))
            }
        }
    }

    suspend fun insertWallet(wallet: Wallet) {
        withContext(Dispatchers.IO) {
            val roomWallet = wallet.toRoomWallet()
            val roomCrypto = wallet.crypto.toRoomCrypto()
            appDatabase.cryptoDao().insert(roomCrypto)
            appDatabase.walletDao().insert(roomWallet)
        }
    }

    suspend fun updateAllWallets(wallets: List<Wallet>) {
        withContext(Dispatchers.IO) {
            val roomWallets = wallets.map { it.toRoomWallet() }
            val roomCryptos = wallets.map { it.crypto }.toSet().map { it.toRoomCrypto() }
            appDatabase.walletDao().updateAll(roomWallets)
            appDatabase.cryptoDao().updateAll(roomCryptos)
        }
    }

    suspend fun deleteWallet(wallet: Wallet) {
        withContext(Dispatchers.IO) {
            val roomWallet = wallet.toRoomWallet()
            appDatabase.walletDao().delete(roomWallet)
        }
    }

}