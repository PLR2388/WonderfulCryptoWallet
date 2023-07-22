package fr.wonderfulappstudio.wonderfulcryptowallet.data.network.datasource

import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitCoinGeckoNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitNowNodesNetwork
import javax.inject.Inject
import kotlin.math.pow

class RemoteDataSource @Inject constructor(
    private val coinGeckoNetwork: RetrofitCoinGeckoNetwork,
    private val nowNodesNetwork: RetrofitNowNodesNetwork
) {

    suspend fun getUSDPrice(name: String): Double? {
        val value = coinGeckoNetwork.getCoinsPrices(listOf(name), listOf("usd"))
        return value[name.lowercase()]?.get("usd")
    }

    suspend fun getBitcoinBalance(address: String): Double {
        val value = nowNodesNetwork.getAddressStat(address = address)
        val balanceString = value.balance
        return balanceString.toDouble() / 10.0.pow(8.0)
    }
}