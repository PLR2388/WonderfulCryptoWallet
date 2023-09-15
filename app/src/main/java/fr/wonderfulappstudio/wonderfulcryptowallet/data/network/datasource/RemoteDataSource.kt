package fr.wonderfulappstudio.wonderfulcryptowallet.data.network.datasource

import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitCoinGeckoNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitBlockchainNetwork
import javax.inject.Inject
import kotlin.math.pow

class RemoteDataSource @Inject constructor(
    private val coinGeckoNetwork: RetrofitCoinGeckoNetwork,
    private val nowNodesNetwork: RetrofitBlockchainNetwork
) {

    suspend fun getUSDPrice(name: String): Double? {
        val value = coinGeckoNetwork.getCoinsPrices(listOf(name), listOf("usd"))
        return value[name.lowercase()]?.get("usd")
    }

    suspend fun getBitcoinBalance(address: String): Double {
        val value = nowNodesNetwork.getAddressStat(address = address)
        val balance = value.addresses[0].final_balance
        return balance.toDouble() / 10.0.pow(8.0)
    }
}