package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCategoryDao(private val initialTasks: MutableList<Category> = mutableListOf()) :
    CategoryDao {

    override suspend fun insert(category: Category): Long {
        initialTasks.add(category)
        return category.id
    }

    override fun getAll(): Flow<List<Category>> {
        return flow {
            emit(initialTasks)
        }
    }

    override suspend fun getByName(name: String): Category? {
       return initialTasks.find { name.equals(it.name, ignoreCase = true) }
    }

    override suspend fun getById(id: Long): Category? {
        return initialTasks.find { id == it.id }
    }
}

