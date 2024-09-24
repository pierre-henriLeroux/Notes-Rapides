package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.UserPreference
import fr.phlab.notesrapides.data.repository.UserPreferenceRepository

class FakeUserPreferenceRepository(private val fakeUserPreferenceDao: FakeUserPreferenceDao) :
    UserPreferenceRepository {
    override suspend fun getUnique(): UserPreference {
        return fakeUserPreferenceDao.getUnique()?: UserPreference(0, null)
    }

    override suspend fun cleanUsers() = fakeUserPreferenceDao.cleanUsers()

    override suspend fun update(userPreference: UserPreference) {
        fakeUserPreferenceDao.update(userPreference)
    }
}