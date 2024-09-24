package fr.phlab.notesrapides.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.data.repository.PlaceRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacePickerViewModel @Inject constructor(private val placeRepository: PlaceRepository) :
    ViewModel() {

    var currentPlace: MutableState<Place?> = mutableStateOf(null)
        private set

    var places: MutableState<List<Place>> = mutableStateOf(emptyList())
        private set

    init {
        initPlaces()
    }

    private fun initPlaces() = viewModelScope.launch {
        places.value = placeRepository.getAll()
    }


    fun initCurrentPlace(currentPlaceId: Long?) = viewModelScope.launch {
        if(currentPlaceId == null){
            currentPlace.value = null
        }
        else {
            currentPlace.value = placeRepository.getById(currentPlaceId)
        }
    }

    fun updateCurrentPlaceNameAndId(newName: String, newId: Long?) {
        if (newName.isEmpty()) {
            currentPlace.value = null
            return
        }
        val newPlace = if (newId == null) {
            Place(0, newName)
        } else {
            places.value.find { it.id == newId }
        }
        currentPlace.value = newPlace
    }

}