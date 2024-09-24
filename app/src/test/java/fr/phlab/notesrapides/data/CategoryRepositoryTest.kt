package fr.phlab.notesrapides.data

import com.google.common.truth.Truth.assertThat
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.repository.CategoryBddRepository
import fr.phlab.notesrapides.data.repository.CategoryRepository
import fr.phlab.shared_test.data.FakeCategoryDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryTest {

    private val category1 = Category(id = 1, name = "Category 1", isPref = true)
    private val category2 = Category(id = 2, name = "Category 2", isPref = false)

    private lateinit var dataSource1: FakeCategoryDao
    private lateinit var categoryRepository: CategoryRepository

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)


    @Before
    fun createRepository() {
        dataSource1 = FakeCategoryDao(mutableListOf(category1, category2))
        categoryRepository = CategoryBddRepository(dataSource1)
    }

    @Test
    fun getAll_returnsAllCategories() =  testScope.runTest {
        val all: List<Category> = categoryRepository.getAll().single()
        assertThat(all.size).isEqualTo(2)
        assertThat(all).containsExactly(category1, category2)
    }

    @Test
    fun getById_returnsCategory() =  testScope.runTest {
        val existingC = categoryRepository.getById(1)
        assertThat(existingC).isEqualTo(category1)

        val nonExistingC = categoryRepository.getById(3)
        assertThat(nonExistingC).isNull()
    }


}