package fr.phlab.notesrapides.viewModel

import com.google.common.truth.Truth.assertThat
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.ui.CategoryPickerViewModel
import fr.phlab.notesrapides.ui.PlacePickerViewModel
import fr.phlab.shared_test.MainCoroutineRule
import fr.phlab.shared_test.data.FakeCategoryDao
import fr.phlab.shared_test.data.FakeCategoryRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryPickerViewModelTest {

    private lateinit var categoryPickerViewModelTest: CategoryPickerViewModel

    val category1 = Category(id = 1, name = "Category 1", isPref = false)
    val category2 = Category(id = 2, name = "Category 2", isPref = false)
    val category3 = Category(id = 3, name = "Category 3", isPref = false)
    val category4 = Category(id = 4, name = "Category 4", isPref = false)


    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        val fakeCategoryRepository = FakeCategoryRepository(FakeCategoryDao(mutableListOf(category1, category2, category3, category4)))

        categoryPickerViewModelTest = CategoryPickerViewModel(fakeCategoryRepository)
    }

    @Test
    fun initialisationTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        advanceUntilIdle()

        assertThat(categoryPickerViewModelTest.currentCategory.value).isNull()
        assertThat(categoryPickerViewModelTest.categories.value).hasSize(4)

        categoryPickerViewModelTest.initCurrentCategory(category1.id)
        advanceUntilIdle()
        assertThat(categoryPickerViewModelTest.currentCategory.value?.id).isEqualTo(category1.id)
    }

    @Test
    fun updateCurrentPlaceNameAndIdTest() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        categoryPickerViewModelTest.initCurrentCategory(category1.id)
        advanceUntilIdle()

        categoryPickerViewModelTest.updateCurrentNameAndId("New name", null)
        advanceUntilIdle()
        assertThat(categoryPickerViewModelTest.currentCategory.value?.name).isEqualTo("New name")
        assertThat(categoryPickerViewModelTest.currentCategory.value?.id).isEqualTo(0)

        categoryPickerViewModelTest.initCurrentCategory(category2.id)
        advanceUntilIdle()
        categoryPickerViewModelTest.updateCurrentNameAndId(category3.name, category3.id)
        advanceUntilIdle()
        assertThat(categoryPickerViewModelTest.currentCategory.value?.id).isEqualTo(category3.id)
        assertThat(categoryPickerViewModelTest.currentCategory.value?.name).isEqualTo(category3.name)

        categoryPickerViewModelTest.initCurrentCategory(category2.id)
        advanceUntilIdle()

        categoryPickerViewModelTest.updateCurrentNameAndId(category3.name, category4.id)
        advanceUntilIdle()
        assertThat(categoryPickerViewModelTest.currentCategory.value?.id).isEqualTo(category4.id)
        assertThat(categoryPickerViewModelTest.currentCategory.value?.name).isEqualTo(category4.name)

    }


}