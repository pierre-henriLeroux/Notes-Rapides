package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.UserPreference
import fr.phlab.notesrapides.data.bdd.dao.UserPreferenceDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserBddPreferenceRepository @Inject constructor(private val userPreferenceDao: UserPreferenceDao): UserPreferenceRepository {

    override suspend fun getUnique(): UserPreference
         {
            var userPreference = userPreferenceDao.getUnique()
            if(userPreference == null)
            {
                val newUserPreference = UserPreference(0, null)
                userPreferenceDao.insert(newUserPreference)
                userPreference = userPreferenceDao.getUnique()!!
            }
            return userPreference
        }


    override suspend fun cleanUsers() = userPreferenceDao.cleanUsers()

    override suspend fun update(userPreference: UserPreference) {
        userPreferenceDao.update(userPreference)
    }

}