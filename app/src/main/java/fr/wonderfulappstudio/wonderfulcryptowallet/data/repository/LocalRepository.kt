package fr.wonderfulappstudio.wonderfulcryptowallet.data.repository

import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.LocalDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    suspend fun getAllWallets() = localDataSource.getAllWallets()

    suspend fun insertWallet(wallet: Wallet) = localDataSource.insertWallet(wallet)

    suspend fun deleteWallet(wallet: Wallet) = localDataSource.deleteWallet(wallet)
}