package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.bdd.dao.PlaceDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceBddRepository @Inject constructor(private val placeDao: PlaceDao): PlaceRepository {
    override fun getAllFlow(): Flow<List<Place>> = placeDao.getAllFlow()
    override suspend fun getAll(): List<Place> {
      return placeDao.getAll()
    }

    override suspend fun getById(id: Long): Place? = placeDao.getById(id)

    override suspend fun createOrGetId(newPlace: Place): Long {
        val place = placeDao.getById(newPlace.id)
        if (place != null) {
            return newPlace.id
        }
        val existingPlaceDuplicate = placeDao.getByName(newPlace.name)
        if (existingPlaceDuplicate != null) {
            return existingPlaceDuplicate.id
        }
        val newId = placeDao.insert(newPlace)
        return newId
    }
}