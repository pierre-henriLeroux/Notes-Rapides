package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {

    fun getAllFlow(): Flow<List<Place>>

    suspend fun getAll(): List<Place>

    suspend fun getById(id: Long): Place?
    suspend fun createOrGetId(newPlace: Place): Long
}