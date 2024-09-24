package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.UserPreference
import fr.phlab.notesrapides.data.bdd.dao.UserPreferenceDao

class FakeUserPreferenceDao(private var initialTasks: MutableList<UserPreference> = mutableListOf()) :
    UserPreferenceDao {

    override suspend fun getUnique(uniqueUserId: Long): UserPreference? {
        return initialTasks.firstOrNull()
    }

    override suspend fun cleanUsers() {
        initialTasks = mutableListOf()
    }

    override suspend fun update(userPreference: UserPreference) {
        initialTasks.find { userPreference.id == it.id }
            ?.let { it.prefCategoryId = userPreference.prefCategoryId }
    }

    override suspend fun insert(userPreference: UserPreference) {
        initialTasks.add(userPreference)
    }
}