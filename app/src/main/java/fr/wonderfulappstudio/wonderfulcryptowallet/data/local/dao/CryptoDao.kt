package fr.wonderfulappstudio.wonderfulcryptowallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.wonderfulappstudio.wonderfulcryptowallet.data.local.RoomCrypto


@Dao
interface CryptoDao {

    @Query("SELECT * FROM RoomCrypto")
    fun getAll(): List<RoomCrypto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(crypto: RoomCrypto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(cryptos: List<RoomCrypto>)
}