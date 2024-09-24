package fr.phlab.notesrapides.data

import com.google.common.truth.Truth.assertThat
import fr.phlab.notesrapides.data.bdd.UserPreference
import fr.phlab.notesrapides.data.repository.UserPreferenceRepository
import fr.phlab.shared_test.data.FakeUserPreferenceDao
import fr.phlab.shared_test.data.FakeUserPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferenceRepositoryTest {

    private var userPreference1 = UserPreference(id = 1, prefCategoryId = 1)

    private lateinit var userPreferenceRepository : UserPreferenceRepository
    private lateinit var fakeUserPreferenceDao : FakeUserPreferenceDao

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    @Before
    fun createRepository() {
        fakeUserPreferenceDao = FakeUserPreferenceDao(mutableListOf(userPreference1))
        userPreferenceRepository = FakeUserPreferenceRepository(fakeUserPreferenceDao)
    }

    @Test
    fun getUniquePreference() = testScope.runTest {
        assertThat(userPreferenceRepository.getUnique()).isEqualTo(userPreference1)
        userPreferenceRepository.cleanUsers()
        assertThat(userPreferenceRepository.getUnique()).isEqualTo(UserPreference(0, null))
    }



}