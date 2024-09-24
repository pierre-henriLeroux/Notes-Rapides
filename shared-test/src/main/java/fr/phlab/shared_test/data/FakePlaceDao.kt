package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.bdd.dao.PlaceDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePlaceDao(private val initialTasks: MutableList<Place> = mutableListOf()): PlaceDao {
    override suspend fun getAll(): List<Place> {
      return initialTasks
    }

    override suspend fun insert(place: Place): Long {
        initialTasks.add(place)
        return place.id
    }

    override suspend fun getByName(name: String): Place? {
       return initialTasks.find { name.equals(it.name, ignoreCase = true) }
    }

    override fun getAllFlow(): Flow<List<Place>> {
        return flow { emit(initialTasks) }
    }

    override suspend fun getById(id: Long): Place? {
        return initialTasks.find { it.id == id }
    }
}