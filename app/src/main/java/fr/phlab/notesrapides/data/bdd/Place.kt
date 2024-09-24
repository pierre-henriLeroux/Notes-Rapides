package fr.phlab.notesrapides.data.bdd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    )
