package fr.phlab.notesrapides.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.phlab.notesrapides.data.bdd.dao.CategoryDao
import fr.phlab.notesrapides.data.repository.CategoryBddRepository
import fr.phlab.notesrapides.data.repository.CategoryRepository
import fr.phlab.notesrapides.data.repository.NoteBddRepository
import fr.phlab.notesrapides.data.repository.NoteRepository
import fr.phlab.notesrapides.data.repository.PlaceBddRepository
import fr.phlab.notesrapides.data.repository.PlaceRepository
import fr.phlab.notesrapides.data.repository.UserBddPreferenceRepository
import fr.phlab.notesrapides.data.repository.UserPreferenceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideCategoryRepository(repository: CategoryBddRepository): CategoryRepository

    @Singleton
    @Binds
    abstract fun provideUserPreferenceRepository(repository: UserBddPreferenceRepository): UserPreferenceRepository

    @Singleton
    @Binds
    abstract fun provideNoteRepository(dao: NoteBddRepository): NoteRepository

    @Singleton
    @Binds
    abstract fun providePlaceRepository(dao: PlaceBddRepository): PlaceRepository

}