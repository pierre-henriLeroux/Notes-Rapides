package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
class FakeCategoryRepository(private val fakeCategoryDao: FakeCategoryDao) : CategoryRepository {

    override fun getAll(): Flow<List<Category>> {
        return fakeCategoryDao.getAll()
    }

    override suspend fun createOrGet(newCategory: Category): Category {
        val category = fakeCategoryDao.getById(newCategory.id)
        if (category != null) {
            return category
        }
        val newId = fakeCategoryDao.insert(newCategory)
        return fakeCategoryDao.getById(newId)!!
    }

    override suspend fun getById(id: Long): Category? {
        return fakeCategoryDao.getById(id)
    }
}