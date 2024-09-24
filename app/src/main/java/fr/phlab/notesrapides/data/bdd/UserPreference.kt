package fr.phlab.notesrapides.data.bdd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserPreference(
    @PrimaryKey val id: Long,
    var prefCategoryId: Long? = null,
) {
}