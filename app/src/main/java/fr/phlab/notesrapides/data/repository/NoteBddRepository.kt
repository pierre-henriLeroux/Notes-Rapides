package fr.phlab.notesrapides.data.repository

import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.data.bdd.dao.NoteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteBddRepository @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override fun getAll(): Flow<List<Note>> = noteDao.getAll()
    override suspend fun getById(id: Long): Note? = noteDao.getById(id)

    override suspend fun createNewNote(): Note {
        val newItem = noteDao.insert(Note(0))
        return noteDao.getById(newItem)!!
    }

    override suspend fun delete(id: Long) = noteDao.delete(id)

    override suspend fun update(note: Note) = noteDao.update(note)

}