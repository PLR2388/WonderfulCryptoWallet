package fr.wonderfulappstudio.wonderfulcryptowallet.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.model.BlockchainAddressStat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private const val BtcBlockChainUrl = "https://blockchain.info/multiaddr"

private interface RetrofitNowNodesNetworkApi {
    @GET(value = "?cors=true&active={address}")
    suspend fun getBtcAddressStat(
        @Path("address") address: String,
    ): BlockchainAddressStat
}

@Singleton
class RetrofitBlockchainNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) {
    private val networkApi = Retrofit.Builder()
        .baseUrl(BtcBlockChainUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory(MediaType.get("application/json"))
        )
        .build()
        .create(RetrofitNowNodesNetworkApi::class.java)

    suspend fun getAddressStat(address: String): BlockchainAddressStat {
        return withContext(Dispatchers.IO) {
            networkApi.getBtcAddressStat(address)
        }
    }
}