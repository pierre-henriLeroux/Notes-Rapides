package fr.phlab.notesrapides.viewModel

import com.google.common.truth.Truth.assertThat
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.ui.NoteDetailViewModel
import fr.phlab.notesrapides.ui.PlacePickerViewModel
import fr.phlab.shared_test.MainCoroutineRule
import fr.phlab.shared_test.data.FakeNoteDao
import fr.phlab.shared_test.data.FakeNoteRepository
import fr.phlab.shared_test.data.FakePlaceDao
import fr.phlab.shared_test.data.FakePlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PlacePickerViewModelTest {

    private lateinit var placePickerViewModel: PlacePickerViewModel

    val place1 = Place(id = 1, name = "Place 1")
    val place2 = Place(id = 2, name = "Place 2")
    val place3 = Place(id = 3, name = "Place 3")
    val place4 = Place(id = 4, name = "Place 4")

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        val placeTestRepository = FakePlaceRepository(FakePlaceDao(mutableListOf(place1, place2, place3, place4)))

        placePickerViewModel = PlacePickerViewModel(placeTestRepository)
    }

    @Test
    fun initialisationTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        advanceUntilIdle()

        assertThat(placePickerViewModel.currentPlace.value).isNull()
        assertThat(placePickerViewModel.places.value).hasSize(4)

        placePickerViewModel.initCurrentPlace(place1.id)
        advanceUntilIdle()
        assertThat(placePickerViewModel.currentPlace.value?.id).isEqualTo(place1.id)
    }

    @Test
    fun updateCurrentPlaceNameAndIdTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        placePickerViewModel.initCurrentPlace(place1.id)
        advanceUntilIdle()

        placePickerViewModel.updateCurrentPlaceNameAndId("New name", null)
        advanceUntilIdle()
        assertThat(placePickerViewModel.currentPlace.value?.name).isEqualTo("New name")
        assertThat(placePickerViewModel.currentPlace.value?.id).isEqualTo(0)

        placePickerViewModel.initCurrentPlace(place2.id)
        advanceUntilIdle()
        placePickerViewModel.updateCurrentPlaceNameAndId(place3.name, place3.id)
        advanceUntilIdle()
        assertThat(placePickerViewModel.currentPlace.value?.id).isEqualTo(place3.id)
        assertThat(placePickerViewModel.currentPlace.value?.name).isEqualTo(place3.name)

        placePickerViewModel.initCurrentPlace(place2.id)
        advanceUntilIdle()

        placePickerViewModel.updateCurrentPlaceNameAndId(place3.name, place4.id)
        advanceUntilIdle()
        assertThat(placePickerViewModel.currentPlace.value?.id).isEqualTo(place4.id)
        assertThat(placePickerViewModel.currentPlace.value?.name).isEqualTo(place4.name)

    }
}