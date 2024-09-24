package fr.phlab.notesrapides.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.phlab.notesrapides.data.bdd.AppDatabase
import fr.phlab.notesrapides.data.bdd.dao.CategoryDao
import fr.phlab.notesrapides.data.bdd.dao.NoteDao
import fr.phlab.notesrapides.data.bdd.dao.PlaceDao
import fr.phlab.notesrapides.data.bdd.dao.UserPreferenceDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "NotesRapides.db"
        ).allowMainThreadQueries().build() //allowMainThreadQueries need for androidTest to work
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideUserPreferenceDao(database: AppDatabase): UserPreferenceDao = database.userPreferenceDao()

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()

    @Provides
    fun providePlaceDao(database: AppDatabase): PlaceDao = database.placeDao()
}