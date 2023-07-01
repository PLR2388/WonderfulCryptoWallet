package fr.wonderfulappstudio.wonderfulcryptowallet.data.local

import fr.wonderfulappstudio.wonderfulcryptowallet.model.WalletData
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val appDatabase: AppDatabase
) {

    val wallets: Flow<WalletData> = flow {
        val roomWallets = appDatabase.walletDao().getAll()
        val wallets = roomWallets.map { Wallet.fromRoomWallet(it) }
        emit(WalletData(wallets))
    }

    fun insertWallet(wallet: Wallet) {
        val roomWallet = wallet.toRoomWallet()
        appDatabase.walletDao().insert(roomWallet)
    }

    fun deleteWallet(wallet: Wallet) {
        val roomWallet = wallet.toRoomWallet()
        appDatabase.walletDao().delete(roomWallet)
    }

}