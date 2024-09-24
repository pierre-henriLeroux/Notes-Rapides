package fr.phlab.notesrapides.viewModel

import com.google.common.truth.Truth.assertThat
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.ui.HomeViewModel
import fr.phlab.notesrapides.ui.NoteDetailViewModel
import fr.phlab.notesrapides.utils.toBddString
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
import java.util.Date

@ExperimentalCoroutinesApi
class NoteDetailViewModelTest {

    private lateinit var noteDetailViewModel: NoteDetailViewModel

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
        val noteTestRepository = FakeNoteRepository(FakeNoteDao(mutableListOf(note1, note2, note3, note4)))
        val placeTestRepository = FakePlaceRepository(FakePlaceDao(mutableListOf(place1)))

        noteDetailViewModel = NoteDetailViewModel(noteTestRepository, placeTestRepository)
    }

    @Test
    fun initialisationTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(noteDetailViewModel.currentNote.value).isNull()
        assertThat(noteDetailViewModel.currentPlace.value).isNull()
    }
    @Test
    fun initialisationWithIdTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value).isNull()
        assertThat(noteDetailViewModel.currentPlace.value).isNull()

        //check initialisation note 1
        noteDetailViewModel.initNote(note1.id)
        advanceUntilIdle()

        assertThat(noteDetailViewModel.currentNote.value?.id).isEqualTo(note1.id)
        assertThat(noteDetailViewModel.currentNote.value?.date).isEqualTo(note1.date)
        assertThat(noteDetailViewModel.currentPlace.value).isNull()

        //check initialisation null
        noteDetailViewModel.initNote(null)
        advanceUntilIdle()

        assertThat(noteDetailViewModel.currentNote.value).isNotNull()
        assertThat(noteDetailViewModel.currentPlace.value).isNull()

        //check initialisation note3
        noteDetailViewModel.initNote(note3.id)
        advanceUntilIdle()

        assertThat(noteDetailViewModel.currentNote.value?.id).isEqualTo(note3.id)
        assertThat(noteDetailViewModel.currentNote.value?.date).isEqualTo(note3.date)
        assertThat(noteDetailViewModel.currentPlace.value?.id).isEqualTo(place1.id)
    }

    @Test
    fun updateDateTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        noteDetailViewModel.initNote(note1.id)

        val tmpDate = Date()
        noteDetailViewModel.updateDate(tmpDate)
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value?.date).isEqualTo(tmpDate.toBddString())
    }

    @Test
    fun updatePlaceTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        noteDetailViewModel.initNote(note1.id)
        advanceUntilIdle()
        noteDetailViewModel.updatePlace(place1)
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value?.placeId).isEqualTo(place1.id)
        assertThat(noteDetailViewModel.currentPlace.value?.id).isEqualTo(place1.id)

        noteDetailViewModel.updatePlace(null)
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value?.placeId).isNull()
        assertThat(noteDetailViewModel.currentPlace.value).isNull()
    }

    @Test
    fun updateTitleTest() = runTest {

        Dispatchers.setMain(StandardTestDispatcher())

        noteDetailViewModel.initNote(note1.id)
        advanceUntilIdle()
        noteDetailViewModel.updateTitle("New title")
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value?.title).isEqualTo("New title")
    }

    @Test
    fun updateContentTest() = runTest {

        Dispatchers.setMain(StandardTestDispatcher())

        noteDetailViewModel.initNote(note1.id)
        advanceUntilIdle()
        noteDetailViewModel.updateContent("New content")
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value?.content).isEqualTo("New content")
    }

    @Test
    fun deleteIfEmptyNoteTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        noteDetailViewModel.initNote(null)
        advanceUntilIdle()
        noteDetailViewModel.deleteIfEmptyNote()
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value).isNull()
        assertThat(noteDetailViewModel.currentPlace.value).isNull()

        noteDetailViewModel.initNote(note1.id)
        advanceUntilIdle()
        noteDetailViewModel.deleteNote()
        advanceUntilIdle()
        noteDetailViewModel.initNote(note1.id)
        advanceUntilIdle()
        assertThat(noteDetailViewModel.currentNote.value).isNull()
        assertThat(noteDetailViewModel.currentPlace.value).isNull()

    }





}