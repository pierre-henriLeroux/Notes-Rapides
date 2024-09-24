package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAll(): Flow<List<Note>>

    suspend fun getById(id: Long): Note?

    suspend fun createNewNote() : Note
    suspend fun delete(id: Long)
    suspend fun update(note: Note)
}