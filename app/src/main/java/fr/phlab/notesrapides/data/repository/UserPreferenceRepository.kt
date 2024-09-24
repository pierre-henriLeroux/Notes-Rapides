package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.UserPreference
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {

    suspend fun getUnique(): UserPreference

    suspend fun cleanUsers()

    suspend fun update(userPreference: UserPreference)
}