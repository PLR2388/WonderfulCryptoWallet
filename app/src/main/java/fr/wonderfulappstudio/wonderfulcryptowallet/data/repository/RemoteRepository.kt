package fr.wonderfulappstudio.wonderfulcryptowallet.data.repository

import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.NetworkDataSource
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend fun getCoinsPrices(
        ids: List<String>,
        currencies: List<String>
    ): Map<String, Map<String, Double>> = networkDataSource.getCoinsPrices(ids, currencies)
}