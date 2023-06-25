package fr.wonderfulappstudio.wonderfulcryptowallet.data.repository

import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.LocalDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.model.WalletData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(
    localDataSource: LocalDataSource
) {
    val wallets: Flow<WalletData> = localDataSource.wallets
}