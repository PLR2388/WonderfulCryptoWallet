package fr.wonderfulappstudio.wonderfulcryptowallet.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitCoinGeckoNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitBlockchainNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.datasource.RemoteDataSource
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okhttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun providesCoinGeckoNetworkDatasource(): RetrofitCoinGeckoNetwork =
        RetrofitCoinGeckoNetwork(providesNetworkJson(), okhttpCallFactory())

    @Provides
    @Singleton
    fun providesNowNodesNetworkDatasource(): RetrofitBlockchainNetwork = RetrofitBlockchainNetwork(
        providesNetworkJson(), okhttpCallFactory()
    )

    @Provides
    @Singleton
    fun providesRemoteDataSource(): RemoteDataSource = RemoteDataSource(
        providesCoinGeckoNetworkDatasource(), providesNowNodesNetworkDatasource()
    )
}