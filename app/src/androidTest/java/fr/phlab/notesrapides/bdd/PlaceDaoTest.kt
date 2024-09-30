package fr.phlab.notesrapides.bdd

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import fr.phlab.notesrapides.data.bdd.AppDatabase
import fr.phlab.notesrapides.data.bdd.Place
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PlaceDaoTest {

    //data test
    val place1 = Place(id = 1, name = "Place 1")

    val place2 = Place(id = 2, name = "Place 2")
    val place3 = Place(id = 3, name = "Place 3")
    val place4 = Place(id = 4, name = "Place 4")

    // using an in-memory database
    private lateinit var database: AppDatabase


    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertPlaceAndGetByIdTest() = runTest {

        // GIVEN - insert a task
        database.placeDao().insert(place1)
        database.placeDao().insert(place2)
        database.placeDao().insert(place3)
        database.placeDao().insert(place4)

        val loaded = database.placeDao().getById(place1.id)

        assertEquals(place1.id, loaded!!.id)
        assertEquals(place1.name, loaded.name)
    }

    @Test
    fun insertPlaceAndGetByNameTest() = runTest {

        database.placeDao().insert(place1)
        database.placeDao().insert(place2)
        database.placeDao().insert(place3)
        database.placeDao().insert(place4)

        val loaded = database.placeDao().getByName(place1.name)

        assertEquals(place1.id, loaded!!.id)
        assertEquals(place1.name, loaded.name)
    }

    @Test
    fun getAllTest() = runTest {

        database.placeDao().insert(place1)
        database.placeDao().insert(place2)
        database.placeDao().insert(place3)
        database.placeDao().insert(place4)

        val loaded = database.placeDao().getAll()

        assertEquals(4, loaded.size)

        val loadedFlow = database.placeDao().getAllFlow()
        assertEquals(4, loadedFlow.first().size)
    }

}