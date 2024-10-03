package fr.phlab.notesrapides.utils.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


class Dimensions(
    val viewSpacing: Dp,
    val screenSpacing:Dp,
)
{
    var  noteBoxHeight = 100.dp
    var  noteBoxWidth = 200.dp
}

val compactDimensions = Dimensions(
    viewSpacing = 8.dp,
    screenSpacing = 16.dp,
)

val largeDimensions = Dimensions(
    viewSpacing = 16.dp,
    screenSpacing = 32.dp,
)
