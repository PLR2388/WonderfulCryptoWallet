package fr.wonderfulappstudio.wonderfulcryptowallet.data.repository

import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.LocalDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.model.WalletData
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    val wallets: Flow<WalletData> = localDataSource.wallets

    fun insertWallet(wallet: Wallet) = localDataSource.insertWallet(wallet)

    fun deleteWallet(wallet: Wallet) = localDataSource.deleteWallet(wallet)
}