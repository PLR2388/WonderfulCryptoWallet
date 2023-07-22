package fr.wonderfulappstudio.wonderfulcryptowallet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.RoomWallet

@Dao
interface WalletDao {
    @Query("SELECT * FROM RoomWallet")
    fun getAll(): List<RoomWallet>

    @Insert
    fun insert(wallet: RoomWallet)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(wallets: List<RoomWallet>)

    @Delete
    fun delete(wallet: RoomWallet)
}