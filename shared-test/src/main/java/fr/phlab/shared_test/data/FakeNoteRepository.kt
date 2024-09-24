package fr.phlab.shared_test.data

import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class FakeNoteRepository(private val fakeNoteDao: FakeNoteDao): NoteRepository {
    override fun getAll(): Flow<List<Note>> = fakeNoteDao.getAll()
    override suspend fun getById(id: Long): Note? = fakeNoteDao.getById(id)
    override suspend fun delete(id: Long) {
        fakeNoteDao.delete(id)
    }

    override suspend fun update(note: Note) {
        fakeNoteDao.update(note)
    }

    override suspend fun createNewNote(): Note {
        fakeNoteDao.insert(Note(-1))
        return Note(-1)
    }
}