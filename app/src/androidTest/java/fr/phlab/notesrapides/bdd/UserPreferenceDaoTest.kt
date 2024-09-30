package fr.phlab.notesrapides.bdd

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import fr.phlab.notesrapides.data.bdd.AppDatabase
import fr.phlab.notesrapides.data.bdd.UserPreference
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserPreferenceDaoTest {

    //data test
    private val userPreference1 = UserPreference(id = 1, prefCategoryId = 1)
    private val userPreference1Bis = UserPreference(id = 1, prefCategoryId = 3)

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
    fun insertUserPreferenceAndGetTest() = runTest {

        database.userPreferenceDao().insert(userPreference1)

        val loaded = database.userPreferenceDao().getUnique(userPreference1.id)

        assertEquals(userPreference1.id, loaded!!.id)
        assertEquals(userPreference1.prefCategoryId, loaded.prefCategoryId)

    }

    @Test
    fun updateUserPreferenceTest() = runTest {

        database.userPreferenceDao().insert(userPreference1)
        database.userPreferenceDao().update(userPreference1Bis)

        val loaded = database.userPreferenceDao().getUnique(userPreference1.id)

        assertEquals(userPreference1Bis.id, loaded!!.id)
        assertEquals(userPreference1Bis.prefCategoryId, loaded.prefCategoryId)
    }

    @Test
    fun cleanUsersTest() = runTest {

        database.userPreferenceDao().insert(userPreference1)
        database.userPreferenceDao().cleanUsers()

        val loaded = database.userPreferenceDao().getUnique(userPreference1.id)

        assertEquals(null, loaded)
    }


}