package fr.phlab.notesrapides.bdd

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import fr.phlab.notesrapides.data.bdd.AppDatabase
import fr.phlab.notesrapides.data.bdd.Note
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
class NoteDaoTest {

    //data test
    val note1 = Note(id = 1, date = "30/08/2024", placeId = null, categoryId = 1)
    val note1Bis = Note(
        id = 1,
        date = "30/10/2024",
        placeId = 1,
        categoryId = 2,
        title = "Note 1 bis",
        content = "Note 1 bis content"
    )
    val note2 = Note(id = 2, date = "01/09/2024", placeId = null, categoryId = 1)
    val note3 = Note(id = 3, date = null, placeId = 1, categoryId = 1)
    val note4 = Note(id = 4, date = "01/09/2024", placeId = 1, categoryId = 2)

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
    fun insertNoteAndGetByIdTest() = runTest {
        // GIVEN - insert a task
        database.noteDao().insert(note1)
        database.noteDao().insert(note2)

        // WHEN - Get the task by id from the database
        val loaded = database.noteDao().getById(note1.id)

        // THEN - The loaded data contains the expected values
        assertNotNull(loaded as Note)

        assertEquals(note1.id, loaded.id)
        assertEquals(note1.date, loaded.date)
        assertEquals(note1.placeId, loaded.placeId)
        assertEquals(note1.categoryId, loaded.categoryId)
    }

    @Test
    fun updateNoteTest() = runTest {

        // GIVEN - insert a task
        database.noteDao().insert(note1)
        database.noteDao().insert(note2)

        database.noteDao().update(note1Bis)
        val loaded = database.noteDao().getById(note1.id)

        assertEquals(note1Bis.id, loaded!!.id)
        assertEquals(note1Bis.date, loaded.date)
        assertEquals(note1Bis.placeId, loaded.placeId)
        assertEquals(note1Bis.categoryId, loaded.categoryId)
        assertEquals(note1Bis.title, loaded.title)
        assertEquals(note1Bis.content, loaded.content)
    }

    @Test
    fun deleteNoteTest() = runTest {
        // GIVEN - insert a task
        database.noteDao().insert(note1)

        database.noteDao().delete(note1.id)
        val loaded = database.noteDao().getById(note1.id)

        assertEquals(null, loaded)
    }

    @Test
    fun getAllTest() = runTest {

        // GIVEN - insert a task
        database.noteDao().insert(note1)
        database.noteDao().insert(note2)
        database.noteDao().insert(note3)
        database.noteDao().insert(note4)

        val loaded = database.noteDao().getAll().first()

        assertEquals(4, loaded.size)
    }


}