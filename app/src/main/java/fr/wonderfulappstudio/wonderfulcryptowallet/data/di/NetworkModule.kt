package fr.wonderfulappstudio.wonderfulcryptowallet.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.NetworkDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.data.network.RetrofitCoinGeckoNetwork
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.RemoteRepository
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json

    @Provides
    @Singleton
    fun okhttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun providesNetworkDatasource(): NetworkDataSource =
        RetrofitCoinGeckoNetwork(providesNetworkJson(), okhttpCallFactory())

    @Provides
    @Singleton
    fun providesRemoteRepository(): RemoteRepository = RemoteRepository(providesNetworkDatasource())
}