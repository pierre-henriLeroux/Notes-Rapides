package fr.phlab.notesrapides.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.repository.NoteRepository
import fr.phlab.notesrapides.data.repository.PlaceRepository
import fr.phlab.notesrapides.utils.toBddString
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val placeRepository: PlaceRepository
) :
    ViewModel() {

    var currentNote: MutableState<Note?> = mutableStateOf(null)
        private set
    var currentPlace: MutableState<Place?> = mutableStateOf(null)
        private set

    fun initNote(currentNoteId: Long?) = viewModelScope.launch { //todo exception
        if (currentNoteId == null) {
            currentNote.value = noteRepository.createNewNote()
        } else {
            currentNote.value = noteRepository.getById(currentNoteId)
            currentNote.value?.placeId?.let {
                currentPlace.value = placeRepository.getById(it)
            }
        }
    }

    fun updateDate(newDate: Date?) = viewModelScope.launch {
        currentNote.value?.date = newDate?.toBddString()
        updateNoteBdd()
    }

    fun updatePlace(newPlace: Place?) = viewModelScope.launch {
        if (newPlace == null) {
            currentPlace.value = null
            currentNote.value?.placeId = null
        }
        else
        {
            currentNote.value?.placeId = placeRepository.createOrGetId(newPlace)
            currentPlace.value = placeRepository.getById(currentNote.value!!.placeId!!)
        }
        updateNoteBdd()
    }

    fun updateTitle(newTitle: String) = viewModelScope.launch {
        currentNote.value?.title = newTitle
        updateNoteBdd()
    }

    suspend fun updateNoteBdd() {
        currentNote.value?.let { noteRepository.update(it) }
    }

    fun updateContent(newContent: String) = viewModelScope.launch {
        currentNote.value?.let {
            it.content = newContent
            noteRepository.update(it)
        }
    }

    fun deleteIfEmptyNote() = viewModelScope.launch {
        currentNote.value?.let {
            if (it.title.isNullOrEmpty() && it.content.isNullOrEmpty()) {
                noteRepository.delete(it.id)
                currentNote.value = null
            }
        }
    }

    fun deleteNote() = viewModelScope.launch {
        currentNote.value?.let {
            noteRepository.delete(it.id)
            currentNote.value = null
        }
    }

}