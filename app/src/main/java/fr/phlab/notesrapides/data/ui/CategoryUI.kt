package fr.phlab.notesrapides.data.ui

import fr.phlab.notesrapides.utils.StringResource

data class CategoryUI(
    val id: Long,
    val name: StringResource,
    val isDefault: Boolean,
)
