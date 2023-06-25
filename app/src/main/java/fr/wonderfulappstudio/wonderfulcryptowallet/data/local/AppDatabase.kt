package fr.wonderfulappstudio.wonderfulcryptowallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.dao.WalletDao

@Database(entities = [RoomWallet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
}