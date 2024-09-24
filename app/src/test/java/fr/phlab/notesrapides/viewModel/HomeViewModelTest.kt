package fr.phlab.notesrapides.viewModel

import com.google.common.truth.Truth.assertThat
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.bdd.UserPreference
import fr.phlab.notesrapides.ui.HomeViewModel
import fr.phlab.shared_test.MainCoroutineRule
import fr.phlab.shared_test.data.FakeCategoryRepository
import fr.phlab.shared_test.data.FakeCategoryDao
import fr.phlab.shared_test.data.FakeNoteDao
import fr.phlab.shared_test.data.FakeNoteRepository
import fr.phlab.shared_test.data.FakePlaceDao
import fr.phlab.shared_test.data.FakePlaceRepository
import fr.phlab.shared_test.data.FakeUserPreferenceDao
import fr.phlab.shared_test.data.FakeUserPreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    val category1 = Category(id = 1, name = "Category 1", isPref = true)
    val category2 = Category(id = 2, name = "Category 2", isPref = false)

    val userPreference1 = UserPreference(id = 1, prefCategoryId = 1)

    val note1 = Note(id = 1, date = "30/08/2024", placeId = null, categoryId = 1)
    val note2 = Note(id = 2, date = "01/09/2024", placeId = null, categoryId = 1)
    val note3 = Note(id = 3, date = null, placeId = 1, categoryId = 1)
    val note4 = Note(id = 3, date = "01/09/2024", placeId = 1, categoryId = 2)

    val place1 = Place(id = 1, name = "Place 1")

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        val categoryTestRepository =
            FakeCategoryRepository(FakeCategoryDao(mutableListOf(category1, category2)))
        val userPreferenceTestRepository =
            FakeUserPreferenceRepository(FakeUserPreferenceDao(mutableListOf(userPreference1)))
        val noteTestRepository = FakeNoteRepository(FakeNoteDao(mutableListOf(note1, note2, note3, note4)))

        homeViewModel = HomeViewModel(
            categoryTestRepository,
            userPreferenceTestRepository,
            noteTestRepository
        )
    }

    @Test
    fun initialisationTest() = runTest {

        Dispatchers.setMain(StandardTestDispatcher())

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(homeViewModel.currentUserPref.value?.prefCategoryId).isEqualTo(1L)
        assertThat(homeViewModel.currentCategory.value?.id).isEqualTo(1L)
        assertThat(homeViewModel.notesByCategory.first()).hasSize(3)
        assertThat(homeViewModel.notesByDate.first()).hasSize(2)
    }

    @Test
    fun updateCurrentCategory() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        homeViewModel.updateCurrentCategory(category2)
        advanceUntilIdle()

        assertThat(homeViewModel.currentCategory.value?.id).isEqualTo(2L)
        assertThat(homeViewModel.notesByCategory.first()).hasSize(1)
        assertThat(homeViewModel.notesByDate.first()).hasSize(1)
        assertThat(homeViewModel.currentUserPref.value?.prefCategoryId).isEqualTo(2L)

    }

}