package fr.phlab.notesrapides.bdd

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import fr.phlab.notesrapides.data.bdd.AppDatabase
import fr.phlab.notesrapides.data.bdd.Category
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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
class CategoryDaoTest {

    //data test
    private val category1 = Category(id = 1, name = "Category 1", isPref = true)
    private val category1Bis = Category(id = 1, name = "Category 1 bis", isPref = true)
    private val category2 = Category(id = 2, name = "Category 2", isPref = false)

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
    fun insertCategoryAndGetById() = runTest {
        // GIVEN - insert a task
        database.categoryDao().insert(category1)


        // WHEN - Get the task by id from the database
        val loaded = database.categoryDao().getById(category1.id)

        // THEN - The loaded data contains the expected values
        assertNotNull(loaded as Category)

        assertEquals(category1.id, loaded.id)
        assertEquals(category1.name, loaded.name)
        assertEquals(category1.isPref, loaded.isPref)
    }

    @Test
    fun insertTaskReplacesOnConflict() = runTest {
        // Given that a task is inserted

        database.categoryDao().insert(category1)
        database.categoryDao().insert(category1Bis)

        // THEN - The loaded data contains the expected values
        val loaded = database.categoryDao().getById(category1.id)
        assertEquals(category1Bis.id, loaded?.id)
        assertEquals(category1Bis.name, loaded?.name)
        assertEquals(category1Bis.isPref, loaded?.isPref)
    }

    @Test
    fun getAllTest() = runTest {

        database.categoryDao().insert(category1)
        database.categoryDao().insert(category2)

        val loaded = database.categoryDao().getAll().first()

        assertEquals(2, loaded.size)

    }


}
