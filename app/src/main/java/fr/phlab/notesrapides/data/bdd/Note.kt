package fr.phlab.notesrapides.data.bdd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var date: String? = null,
    var placeId: Long? = null,
    var categoryId: Long? = null,
    var title: String? = null,
    var content: String? = null,//todo content with image
)

