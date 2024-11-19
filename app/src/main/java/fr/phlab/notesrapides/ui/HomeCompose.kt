package fr.phlab.notesrapides.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.phlab.notesrapides.R
import fr.phlab.notesrapides.data.UiGroupedNotes
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.Note
import fr.phlab.notesrapides.utils.style.DarkAppColors
import fr.phlab.notesrapides.utils.style.LightAppColors
import fr.phlab.notesrapides.utils.style.MTextStyle
import fr.phlab.notesrapides.utils.style.Theme
import java.util.Locale

@Composable
fun HomeView(
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentCategory = viewModel.currentCategory.collectAsState(initial = null)
    val uiNotesByDate = viewModel.notesByDate.collectAsState(initial = emptyList())
    val uiNotes = viewModel.notesByCategory.collectAsState(initial = emptyList())

    val isDateExpanded = remember { mutableStateOf(true) }

    Column {
        TopAppBar(
            backgroundColor = Theme.colors.primary,
            contentColor = Theme.colors.onPrimary,
            title = {
                CategorySelector(
                    currentCategory = currentCategory.value,
                    onContentColor = Theme.colors.onPrimary,
                    updateCurrentCategory = {
                        viewModel.updateCurrentCategory(it)
                    }
                )
            }
        )

        Box(
            modifier = Modifier
                .padding(Theme.dimens.screenSpacing)
                .background(Theme.colors.surface)
                .fillMaxSize()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                NoteListGrouped(
                    notes = uiNotesByDate.value,
                    title = stringResource(id = R.string.home_order_by_date),
                    onNoteClick = onNoteClick,
                    isExpanded = isDateExpanded.value,
                    onExpandClick = { isDateExpanded.value = !isDateExpanded.value }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NoteList(
                    notes = uiNotes.value,
                    onNoteClick = onNoteClick
                )
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp),
                text = {
                    Text(text = stringResource(id = R.string.note_create))
                },
                contentColor = Theme.colors.onSecondary,
                backgroundColor = Theme.colors.secondary,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.note_create)
                    )
                },
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    onAddNote()
                })
        }
    }
}

@Composable
fun CategorySelector(
    modifier: Modifier = Modifier,
    onContentColor: Color,
    currentCategory: Category?,
    updateCurrentCategory: (Category?) -> Unit
) {
    val dialogCategoryPicker = remember { mutableStateOf(false) }
    if (dialogCategoryPicker.value) {
        DialogCategoryPicker(
            currentCategoryId = currentCategory?.id,
            onDismiss = { dialogCategoryPicker.value = false },
            onSelect = {
                dialogCategoryPicker.value = false
                updateCurrentCategory(it)
            })
    }
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            dialogCategoryPicker.value = true
        }) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.category_selector_title),
            style = MTextStyle.categorySelectorTitle,
            color =onContentColor
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = (currentCategory?.name
                ?: stringResource(id = R.string.category_all_name)).uppercase(Locale.ROOT),
            style = MTextStyle.categorySelectorTitle,
            color = onContentColor
        )
        Icon(
            modifier = modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            tint = onContentColor,
            painter = painterResource(R.drawable.ic_category),
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
    }


}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TopAppBarCategorySelectorPreviewNight() {
    TopAppBar(
        backgroundColor = Theme.colors.primary,
        contentColor = Theme.colors.onPrimary,
        title = {
            CategorySelector(
                currentCategory = Category(1, "Category Name", true),
                updateCurrentCategory = {},
                onContentColor = DarkAppColors.onPrimary
            )
        }
    )

}


@Preview(showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TopAppBarCategorySelectorPreview() {
    TopAppBar(
        backgroundColor = Theme.colors.primary,
        contentColor = Theme.colors.onPrimary,
        title = {
            CategorySelector(
                currentCategory = Category(1, "Category Name", true),
                updateCurrentCategory = {},
                onContentColor = LightAppColors.onPrimary

            )
        }
    )
}

@Composable
fun NoteList(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onNoteClick: (Note) -> Unit = {}
) {
    Column(modifier = modifier) {
        if (notes.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.defaultMinSize(minHeight = 100.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(items = notes)
                {
                    ItemUINote(item = it, onNoteClick = onNoteClick)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.note_list_empty),
                style = MTextStyle.noteContent
            )
        }

    }
}


@Composable
fun NoteListGrouped(
    modifier: Modifier = Modifier,
    notes: List<UiGroupedNotes>,
    title: String,
    isExpanded: Boolean = true,
    onNoteClick: (Note) -> Unit = {},
    onExpandClick: () -> Unit = {}
) {

    var multiNoteDialogExpand by remember { mutableStateOf(false) }
    var currentUiGroupedNotes by remember { mutableStateOf<UiGroupedNotes?>(null) }

    if (multiNoteDialogExpand) {
        MultiNoteDialogPicker(
            notes = currentUiGroupedNotes?.notes ?: listOf(),
            onSelectNote = {
                multiNoteDialogExpand = false
                onNoteClick(it)
            }, onDismiss = {
                multiNoteDialogExpand = false
            })

    }

    Column(modifier = modifier.border(1.dp, Theme.colors.colorBorder)) {
        Row(
            Modifier
                .border(1.dp, Theme.colors.colorBorder)
                .clickable {
                    onExpandClick()
                }) {
            Text(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .align(Alignment.CenterVertically),
                text = title,
                style = MTextStyle.noteGroupedTitle
            )
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        if (isExpanded) {
            if (notes.isEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    text = stringResource(id = R.string.note_list_empty),
                    style = MTextStyle.noteContent
                )
            } else {
                Box(modifier = Modifier.padding(8.dp)) {
                    LazyRow(
                        modifier = Modifier.defaultMinSize(minHeight = 100.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(items = notes)
                        {
                            ItemUiGroupedNotes(
                                item = it,
                                onNoteGroupedClick = { newUiGroupedNotes ->
                                    currentUiGroupedNotes = newUiGroupedNotes
                                    multiNoteDialogExpand = true
                                })
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemUINotePreview() {
    ItemUINote(
        item = Note(
            1,
            "30/05/2024",
            null,
            1,
            title = "test",
            content = "content content content content content content  content content  content  content content  content content content"
        ),
        onNoteClick = {}
    )
}

@Composable
fun ItemUINote(item: Note, onNoteClick: (Note) -> Unit) {
    Card(modifier = Modifier.clickable { onNoteClick(item) }) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .width(Theme.dimens.noteBoxWidth)
                .height(Theme.dimens.noteBoxHeight)
        ) {
            Column {
                Text(text = item.title ?: "", style = MTextStyle.noteTitle)
                Text(
                    text = item.content ?: "",
                    style = MTextStyle.noteContent,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListDatePreview() {
    NoteListGrouped(
        notes = listOf(
            UiGroupedNotes(
                "30/05/2024", listOf(
                    Note(1, "30/05/2024", null, 1, title = "test1", content = null),
                    Note(2, "30/05/2024", null, 1, title = "test2", content = null),
                    Note(3, "30/05/2024", null, 1, title = "test3", content = null)
                )
            ), UiGroupedNotes(
                "30/05/2024", listOf(
                    Note(4, "30/05/2024", null, 1, title = "test1", content = null),
                    Note(5, "30/05/2024", null, 1, title = "test2", content = null),
                    Note(6, "30/05/2024", null, 1, title = "test3", content = null)
                )
            ), UiGroupedNotes(
                "30/05/2024", listOf(
                    Note(7, "30/05/2024", null, 1, title = "test1", content = null),
                    Note(8, "30/05/2024", null, 1, title = "test2", content = null),
                    Note(9, "30/05/2024", null, 1, title = "test3", content = null)
                )
            )
        ),
        title = "Test Title",
        onNoteClick = {}
    )
}


@Composable
fun ItemUiGroupedNotes(item: UiGroupedNotes, onNoteGroupedClick: (UiGroupedNotes) -> Unit) {
    Card(modifier = Modifier.clickable { onNoteGroupedClick(item) }) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(100.dp)
        ) {
            Column {
                Text(text = item.title, style = MTextStyle.noteGroupedTitle)
                Text(
                    text = pluralStringResource(
                        id = R.plurals.note_grouped_nb,
                        count = item.notes.size,
                        item.notes.size
                    ), style = MTextStyle.noteContent
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    //.border(1.dp, Theme.colors.onSurface)
                    .fillMaxWidth()
            ) {
                item.notes.take(3).forEach {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 4.dp, end = 4.dp)
                            .border(0.5.dp, Theme.colors.onSurface)
                    )
                    {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            text = it.title ?: "",
                            style = MTextStyle.noteContent,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
                if (item.notes.size > 3) {
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .border(1.dp, Theme.colors.onSurface)
                    )
                    {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            text = "... ${item.notes.size - 3}",
                            style = MTextStyle.contentItalic,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemUiGroupedNotesPreview() {
    ItemUiGroupedNotes(
        item = UiGroupedNotes(
            "30/05/2024", listOf(
                Note(1, "30/05/2024", null, 1, title = "test1", content = null),
                Note(2, "30/05/2024", null, 1, title = "test2", content = null),
                Note(3, "30/05/2024", null, 1, title = "test3", content = null),
                Note(3, "30/05/2024", null, 1, title = "test3", content = null),
                Note(3, "30/05/2024", null, 1, title = "test3", content = null)

            )
        ), onNoteGroupedClick = {}
    )
}


