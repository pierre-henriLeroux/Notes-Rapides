package fr.phlab.notesrapides.data.bdd.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.phlab.notesrapides.data.bdd.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place): Long

    @Query("SELECT * FROM place WHERE name LIKE :name")
    suspend fun getByName(name: String): Place?

    @Query("SELECT * FROM place")
    fun getAllFlow(): Flow<List<Place>>

    @Query("SELECT * FROM place")
    suspend fun getAll(): List<Place>

    @Query("SELECT * FROM place WHERE id = :id")
    suspend fun getById(id: Long): Place?


}