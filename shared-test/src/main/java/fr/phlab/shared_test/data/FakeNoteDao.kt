package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.dao.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteDao(private val initialTasks: MutableList<Note> = mutableListOf()) : NoteDao {
    override suspend fun insert(note: Note): Long {
        val newNote = Note(id = (initialTasks.size + 1).toLong())
        initialTasks.add(newNote)
        return newNote.id
    }

    override suspend fun update(note: Note) {
        initialTasks.find { it.id == note.id }?.let {
            it.title = note.title
            it.content = note.content
        }
    }

    override suspend fun delete(id: Long) {
        initialTasks.removeIf { it.id == id }
    }

    override fun getAll(): Flow<List<Note>> =
        flow { emit(initialTasks) }

    override suspend fun getById(id: Long): Note? {
        return initialTasks.find { it.id == id }
    }

}