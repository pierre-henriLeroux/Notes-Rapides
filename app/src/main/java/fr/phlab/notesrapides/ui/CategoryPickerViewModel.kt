package fr.phlab.notesrapides.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.repository.CategoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryPickerViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {

    var currentCategory: MutableState<Category?> = mutableStateOf(null)
        private set

    var categories: MutableState<List<Category>> = mutableStateOf(emptyList())
        private set

    init {
        initCategories()
    }

    fun initCurrentCategory(currentCategoryId: Long?) = viewModelScope.launch {
        if (currentCategoryId == null) {
            currentCategory.value = null
            return@launch
        } else {
            currentCategory.value = categoryRepository.getById(currentCategoryId)
        }
    }

    fun initCategories() = viewModelScope.launch {
        categoryRepository.getAll().collect {
            categories.value = it
        }
    }

    fun updateCurrentNameAndId(newName: String, newId: Long?) {
        if (newName.isEmpty()) {
            currentCategory.value = null
            return
        }
        val newCategory = if (newId == null) {
            Category(0, name = newName, isPref = false)
        } else {
            categories.value.find { it.id == newId }
        }
        currentCategory.value = newCategory
    }

}