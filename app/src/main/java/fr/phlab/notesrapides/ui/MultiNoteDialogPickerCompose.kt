package fr.phlab.notesrapides.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.phlab.notesrapides.R
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.utils.style.Theme


@Composable
fun MultiNoteDialogPicker(notes: List<Note>, onSelectNote: (Note) -> Unit, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .background(Theme.colors.surface)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(all = 16.dp),
                columns = GridCells.Adaptive(minSize = Theme.dimens.noteBoxWidth)
            ) {
                items(items = notes) { note: Note ->
                    Column {
                        ItemUINote(item = note, onNoteClick = {
                            onSelectNote(note)
                        })
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                }
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.cancel), color = Theme.colors.onSurface)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MultiNoteDialogPickerPreview() {
    MultiNoteDialogPicker(
        notes = listOf(
            Note(
                1,
                "30/05/2024",
                null,
                1,
                title = "test1",
                content = null
            ),
            Note(2, "30/05/2024", null, 1, title = "test2", content = null),
            Note(3, "30/05/2024", null, 1, title = "test3", content = null),
            Note(4, "30/05/2024", null, 1, title = "test4", content = null)
        ), onSelectNote = {}, onDismiss = {})
}