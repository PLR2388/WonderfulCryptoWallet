package fr.wonderfulappstudio.wonderfulcryptowallet.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.wonderfulappstudio.wonderfulcryptowallet.BuildConfig
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.model.NowNodesAddressStat
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private const val BtcNowNodesUrl = "https://btcbook.nownodes.io/api/v2/"

private interface RetrofitNowNodesNetworkApi {
    @Headers("api-key : ${BuildConfig.NOW_NODES_API_KEY}")
    @GET(value = "address/{address}")
    suspend fun getBtcAddressStat(
        @Path("address") address: String,
    ): NetworkResponse<NowNodesAddressStat>
}

@Singleton
class RetrofitNowNodesNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) {
    private val networkApi = Retrofit.Builder()
        .baseUrl(BtcNowNodesUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory(MediaType.get("application/json"))
        )
        .build()
        .create(RetrofitNowNodesNetworkApi::class.java)

    suspend fun getAddressStat(address: String): NowNodesAddressStat =
        networkApi.getBtcAddressStat(address).data
}