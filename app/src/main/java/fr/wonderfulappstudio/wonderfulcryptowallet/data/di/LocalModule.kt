package fr.wonderfulappstudio.wonderfulcryptowallet.data.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.AppDatabase
import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.LocalDataSource
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.LocalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "wonderful-wallet-db"
        ).build()

    @Provides
    @Singleton
    fun provideLocalRepository(localDataSource: LocalDataSource) = LocalRepository(localDataSource)
}