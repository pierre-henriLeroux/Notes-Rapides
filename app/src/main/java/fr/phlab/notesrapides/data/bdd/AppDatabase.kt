package fr.phlab.notesrapides.data.bdd

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.phlab.notesrapides.data.bdd.dao.CategoryDao
import fr.phlab.notesrapides.data.bdd.dao.NoteDao
import fr.phlab.notesrapides.data.bdd.dao.PlaceDao
import fr.phlab.notesrapides.data.bdd.dao.UserPreferenceDao

@Database(entities = [Category::class, Note::class, Place::class, UserPreference::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun userPreferenceDao(): UserPreferenceDao

    abstract fun noteDao(): NoteDao

    abstract fun placeDao(): PlaceDao
}
