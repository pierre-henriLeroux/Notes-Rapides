package fr.phlab.notesrapides.data.bdd.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.phlab.notesrapides.data.bdd.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE name LIKE :name")
    suspend fun getByName(name: String): Category?


    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getById(id: Long): Category?

}