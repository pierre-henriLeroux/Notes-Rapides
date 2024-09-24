package fr.phlab.notesrapides.data.bdd.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.phlab.notesrapides.data.bdd.UserPreference
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferenceDao {
    @Query("SELECT *  FROM userpreference WHERE id = :uniqueUserId")
    suspend fun getUnique(uniqueUserId: Long = 0): UserPreference?

    @Query("DELETE FROM userpreference")
    suspend fun cleanUsers()

    @Update
    suspend fun update(userPreference: UserPreference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userPreference: UserPreference)

}