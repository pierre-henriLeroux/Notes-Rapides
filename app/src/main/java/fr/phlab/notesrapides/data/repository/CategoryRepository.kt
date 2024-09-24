package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.dao.CategoryDao
import fr.phlab.notesrapides.data.ui.CategoryUI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface CategoryRepository
{
    fun getAll(): Flow<List<Category>>

    suspend fun getById(id: Long): Category?

    suspend fun createOrGet(newCategory: Category): Category
}