package fr.phlab.notesrapides.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.phlab.notesrapides.R
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.utils.fromBddToDate
import fr.phlab.notesrapides.utils.style.MTextStyle
import fr.phlab.notesrapides.utils.style.Theme
import fr.phlab.notesrapides.utils.toUiString
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun NoteDetailView(
    onBack: () -> Unit, currentNoteId: Long?, viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val currentNote by viewModel.currentNote
    val currentPlace by viewModel.currentPlace
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentNoteId) {
        viewModel.initNote(currentNoteId)
    }
    Column() {
        TopAppBar(
            backgroundColor = Theme.colors.primary,
            contentColor = Theme.colors.onPrimary,
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.deleteIfEmptyNote()
                    onBack()
                }) {
                    Icon(Icons.Filled.Done, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {
                    scope.launch {
                        viewModel.deleteNote()
                        onBack()
                    }
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            },
            title = {

            }
        )
        Column(
            modifier = Modifier
                .padding(Theme.dimens.screenSpacing)
                .background(Theme.colors.surface)
                .fillMaxSize()
        ) {
            if (currentNote != null) {
                TitleNote(currentNote?.title) {
                    viewModel.updateTitle(it)
                }
                Spacer(modifier = Modifier.height(8.dp))
                DateAndPlace(currentNote?.date?.fromBddToDate(), currentPlace, saveDate = {
                    viewModel.updateDate(it)
                }, savePlace = {
                    viewModel.updatePlace(it)
                })
                Spacer(modifier = Modifier.height(8.dp))

                ContentNote(currentNote?.content) {
                    viewModel.updateContent(it)
                }
            } else {
                LinearProgressIndicator()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateAndPlace(
    date: Date?,
    place: Place?,
    saveDate: (Date?) -> Unit = {},
    savePlace: (Place?) -> Unit = {}
) {
    val currentDateState = remember { mutableStateOf(date?.toUiString()) }
    val currentPlace = remember {
        mutableStateOf(place)
    }
    val openDialogDate = remember { mutableStateOf(false) }
    val openDialogPlace = remember { mutableStateOf(false) }

    if (openDialogDate.value) {
        DialogDatePicker(date,
            onDismiss = {
                openDialogDate.value = false
            }, onSelect = { newDate ->
                currentDateState.value = newDate?.toUiString()
                saveDate(newDate)
                openDialogDate.value = false
            })
    }

    if (openDialogPlace.value) {
        DialogPlacePiker(
            currentPlace = currentPlace.value,
            onDismiss = { openDialogPlace.value = false },
            onSelect = {
                currentPlace.value = it
                savePlace(it)
                openDialogPlace.value = false
            })
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    {
        Row(modifier = Modifier
            .clickable {
                openDialogDate.value = true
            }
            .align(Alignment.CenterVertically)) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
                    .padding(end = 8.dp),
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Date"
            )
            Text(
                modifier = Modifier
                    .border(1.dp, Theme.colors.colorBorder)
                    .padding(16.dp),
                text = currentDateState.value ?: stringResource(id = R.string.date_null),
                style = MTextStyle.noteContent
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier
            .clickable {
                openDialogPlace.value = true
            }
            .padding(start = 8.dp))
        {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
                    .padding(end = 8.dp),
                imageVector = Icons.Filled.Home,
                contentDescription = "Place"
            )
            val visibleText =
                if (currentPlace.value?.name == null || currentPlace.value?.name?.isEmpty() == true) {
                    stringResource(
                        id = R.string.place_null
                    )
                } else {
                    currentPlace.value?.name ?: ""
                }
            Text(
                modifier = Modifier
                    .border(1.dp, Theme.colors.colorBorder)
                    .padding(16.dp),
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                text = visibleText,
                style = MTextStyle.noteContent
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DateAndPlacePreview() {
    DateAndPlace(
        "12/12/2023".fromBddToDate(),
        Place(1, "test test test test test test test test test test")
    )
}


@Composable
fun TitleNote(currentTitle: String?, saveChange: (String) -> Unit) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = currentTitle ?: ""))
    }

    BasicTextField(modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Theme.colors.colorBorder)
        .padding(8.dp),
        value = textFieldValue,
        textStyle = MTextStyle.noteTitle,
        onValueChange = { newValue ->
            textFieldValue = newValue
            saveChange(newValue.text)
        })
}

@Preview(showBackground = true)
@Composable
fun TitleNotePreview() {
    TitleNote("Title") {}
}

@Preview(showBackground = true)
@Composable
fun ContentNotePreview() {
    ContentNote("content",
        {})
}

@Composable
fun ContentNote(currentContent: String?, saveChange: (String) -> Unit) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = currentContent ?: ""))
    }
    BasicTextField(modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Theme.colors.colorBorder)
        .padding(8.dp)
        .fillMaxHeight(),
        singleLine = false,
        value = textFieldValue,
        textStyle = MTextStyle.noteContent,
        onValueChange = { newValue ->
            textFieldValue = newValue
            saveChange(newValue.text)
        })
}

