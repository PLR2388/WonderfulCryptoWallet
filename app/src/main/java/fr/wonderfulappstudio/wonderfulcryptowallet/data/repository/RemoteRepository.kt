package fr.wonderfulappstudio.wonderfulcryptowallet.data.repository

import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitCoinGeckoNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitNowNodesNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.model.NowNodesAddressStat
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val coinGeckoNetwork: RetrofitCoinGeckoNetwork,
    private val nowNodesNetwork: RetrofitNowNodesNetwork
) {
    suspend fun getCoinsPrices(
        ids: List<String>,
        currencies: List<String>
    ): Map<String, Map<String, Double>> = coinGeckoNetwork.getCoinsPrices(ids, currencies)

    suspend fun getBtcAddressStat(address: String): NowNodesAddressStat = nowNodesNetwork.getAddressStat(address)
}