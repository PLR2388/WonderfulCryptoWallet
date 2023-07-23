package fr.wonderfulappstudio.wonderfulcryptowallet.data.repository

import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.LocalDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.datasource.RemoteDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.Crypto
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun fetchAndUpdateData() {
        val wallets = localDataSource.getAllWallets()
        val cryptoList =
            wallets.map { wallet -> wallet.crypto }.toSet().map { elt ->
                val value = remoteDataSource.getUSDPrice(elt.name)
                if (value == null) {
                    elt
                } else {
                    elt.copy(currentPrice = value)
                }
            }

        val updateWallets = wallets.map {
            val balance = getBalance(it.address, it.crypto)
            if (balance != null) {
                it.copy(balance = balance)
            } else {
                it
            }
        }.map { wallet ->
            wallet.copy(crypto = cryptoList.first { it.name == wallet.crypto.name})
        }

        localDataSource.updateAllWallets(updateWallets)
    }

    suspend fun getBalance(address: String, crypto: Crypto): Double? {
        return when (crypto.symbol) {
            "BTC" -> remoteDataSource.getBitcoinBalance(address)
            else -> null
        }
    }
}