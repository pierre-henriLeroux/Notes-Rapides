package fr.phlab.notesrapides.data

import fr.phlab.notesrapides.data.bdd.Note

data class UiGroupedNotes(
    val title: String,
    val notes: List<Note>
) {
}