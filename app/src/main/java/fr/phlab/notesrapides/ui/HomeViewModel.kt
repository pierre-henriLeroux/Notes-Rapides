package fr.phlab.notesrapides.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.phlab.notesrapides.data.UiGroupedNotes
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.bdd.UserPreference
import fr.phlab.notesrapides.data.repository.CategoryRepository
import fr.phlab.notesrapides.data.repository.NoteRepository
import fr.phlab.notesrapides.data.repository.PlaceRepository
import fr.phlab.notesrapides.data.repository.UserPreferenceRepository
import fr.phlab.notesrapides.utils.fromBddToDate
import fr.phlab.notesrapides.utils.toBddString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    var currentUserPref: MutableStateFlow<UserPreference?> = MutableStateFlow(null)
        private set

    var currentCategory: MutableStateFlow<Category?> = MutableStateFlow(null)
        private set

    private var allNotes: Flow<List<Note>> = noteRepository.getAll()

    var notesByCategory: Flow<List<Note>> =
        combine(currentCategory, allNotes) { filterCategory, allNotes ->
            filterNotesByCategory(allNotes, filterCategory?.id)
        }.distinctUntilChanged()
        private set

    var notesByDate: Flow<List<UiGroupedNotes>> = notesByCategory.map { notes ->
        filterNotesByDate(notes)
    }.distinctUntilChanged()
        private set

    init {
        initData()
    }

    private fun initData() = viewModelScope.launch {
        val userPreference = userPreferenceRepository.getUnique()
        currentUserPref.emit(userPreference)
        userPreference.prefCategoryId?.let {
            val category = categoryRepository.getById(it)
            currentCategory.emit(category)
        }
    }

    private fun filterNotesByDate(notes: List<Note>): List<UiGroupedNotes> {
        return notes.filter { it.date != null }.groupBy { it.date?.fromBddToDate() }
            .map { groupedNotes ->
                UiGroupedNotes(groupedNotes.key?.toBddString() ?: "", groupedNotes.value)
            }
    }

    private fun filterNotesByCategory(notes: List<Note>, categoryId: Long?): List<Note> {
        if (categoryId == null) return notes
        return notes.filter { it.categoryId == categoryId }
    }

    //todo
    private fun filterNotesByPlace(notes: List<Note>, places: List<Place>): List<UiGroupedNotes> {
        return notes.filter { it.placeId != null }.groupBy { it.placeId }.map { groupedNotes ->
            UiGroupedNotes(
                places.find { groupedNotes.key == it.id }?.name ?: "",
                groupedNotes.value
            )
        }
    }

    fun updateCurrentCategory(newCategoryFilter: Category?) = viewModelScope.launch {
        if (newCategoryFilter == null) {
            currentCategory.emit(null)
            return@launch
        }
        if (newCategoryFilter.id == 0L) {
            currentCategory.emit(categoryRepository.createOrGet(newCategoryFilter))
            return@launch
        }
        currentUserPref.value?.let {
            it.prefCategoryId = newCategoryFilter.id
            userPreferenceRepository.update(it)
        }

        currentCategory.emit(newCategoryFilter)
    }

}