package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.dao.CategoryDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryBddRepository @Inject constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override fun getAll() = categoryDao.getAll()

    override suspend fun getById(id: Long) = categoryDao.getById(id)

    override suspend fun createOrGet(newCategory: Category): Category {
        val category = categoryDao.getById(newCategory.id)
        if (category != null) {
            return category
        }
        val existingCategoryDuplicate = categoryDao.getByName(newCategory.name)
        if (existingCategoryDuplicate != null) {
            return existingCategoryDuplicate
        }
        val newId = categoryDao.insert(newCategory)
        return categoryDao.getById(newId)!!
    }
}