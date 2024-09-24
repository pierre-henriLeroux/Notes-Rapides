package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow

class FakePlaceRepository(private val fakePlaceDao: FakePlaceDao) : PlaceRepository {


    override fun getAllFlow(): Flow<List<Place>> = fakePlaceDao.getAllFlow()

    override suspend fun getAll(): List<Place> {
        return fakePlaceDao.getAll()
    }

    override suspend fun getById(id: Long): Place? {
        return fakePlaceDao.getById(id)
    }

    override suspend fun createOrGetId(newPlace: Place): Long {
        val place = fakePlaceDao.getById(newPlace.id)
        if (place != null) {
            return place.id
        }
        val newId = fakePlaceDao.insert(newPlace)
        return fakePlaceDao.getById(newId)!!.id
    }
}