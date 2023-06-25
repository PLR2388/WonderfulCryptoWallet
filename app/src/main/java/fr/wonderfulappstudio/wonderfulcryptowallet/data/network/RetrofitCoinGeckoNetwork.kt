package fr.wonderfulappstudio.wonderfulcryptowallet.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitCoinGeckoNetworkApi {
    @GET(value = "simple/price")
    suspend fun getCoinsPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") currencies: String
    ): NetworkResponse<Map<String, Map<String, Double>>>
}

private const val CoinGeckoPublicApiUrl = "https://api.coingecko.com/api/v3/"

@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

@Singleton
class RetrofitCoinGeckoNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : NetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(CoinGeckoPublicApiUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory(MediaType.get("application/json"))
        )
        .build()
        .create(RetrofitCoinGeckoNetworkApi::class.java)


    override suspend fun getCoinsPrices(
        ids: List<String>,
        currencies: List<String>
    ): Map<String, Map<String, Double>> =
        networkApi.getCoinsPrices(
            ids.joinToString(separator = ","),
            currencies.joinToString(separator = ",")
        ).data

}