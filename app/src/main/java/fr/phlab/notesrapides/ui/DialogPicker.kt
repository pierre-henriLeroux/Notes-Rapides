package fr.phlab.notesrapides.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import fr.phlab.notesrapides.R
import fr.phlab.notesrapides.data.bdd.Category
import fr.phlab.notesrapides.data.bdd.Place
import fr.phlab.notesrapides.utils.style.MTextStyle
import fr.phlab.notesrapides.utils.style.Theme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDatePicker(
    currentDate: Date?,
    onDismiss: () -> Unit,
    onSelect: (Date?) -> Unit,
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate?.time ?: System.currentTimeMillis()
    )
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colors.surface)
        ) {

            DatePicker(
                state = dateState,
                colors = DatePickerDefaults.colors(
                    containerColor = Theme.colors.surface
                )
            )
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                onClick = {
                    onSelect(null)
                }
            ) {
                Text(stringResource(id = R.string.date_null), color = Theme.colors.onSurface)
            }
            Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
            {
                TextButton(
                    onClick = {
                        onSelect(dateState.selectedDateMillis?.let { Date(it) })
                    }
                ) {
                    Text(stringResource(id = R.string.ok), color = Theme.colors.onSurface)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(stringResource(id = R.string.cancel), color = Theme.colors.onSurface)
                }
            }
        }
    }

}

@Composable
fun DialogPlacePiker(
    currentPlace: Place?,
    onDismiss: () -> Unit,
    onSelect: (Place?) -> Unit,
    viewModel: PlacePickerViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initCurrentPlace(currentPlace?.id)
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colors.surface)
        ) {
            AutoCompleteSelectBar(
                titleSelector = stringResource(id = R.string.place_place_holder),
                entries = viewModel.places.value.map { Pair(it.name, it.id) },
                currentEntry = currentPlace?.name ?: "",
                updateCurrentEntry = { newName, newId ->
                    viewModel.updateCurrentPlaceNameAndId(newName, newId)
                }
            )
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                onClick = {
                    onSelect(null)
                }
            ) {
                Text(stringResource(id = R.string.place_cancel), color = Theme.colors.onSurface)
            }
            Row()
            {
                TextButton(
                    onClick = {
                        onSelect(viewModel.currentPlace.value)
                    }
                ) {
                    Text(stringResource(id = R.string.ok), color = Theme.colors.onSurface)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(stringResource(id = R.string.cancel), color = Theme.colors.onSurface)
                }
            }
        }
    }
}


@Composable
fun DialogCategoryPicker(
    currentCategoryId: Long?,
    onDismiss: () -> Unit,
    onSelect: (Category?) -> Unit,
    viewModel: CategoryPickerViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initCurrentCategory(currentCategoryId)
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colors.surface)
        ) {
            AutoCompleteSelectBar(
                titleSelector = stringResource(id = R.string.category_selector_title),
                entries = viewModel.categories.value.map { Pair(it.name, it.id) },
                currentEntry = viewModel.currentCategory.value?.name
                    ?: "",
                updateCurrentEntry = { newName, newId ->
                    viewModel.updateCurrentNameAndId(newName, newId)
                }
            )
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                onClick = {
                    onSelect(null)
                }
            ) {
                Text(stringResource(id = R.string.category_all_button), color = Theme.colors.onSurface)
            }
            Row()
            {
                TextButton(
                    onClick = {
                        onSelect(viewModel.currentCategory.value)
                    }
                ) {
                    Text(stringResource(id = R.string.ok), color = Theme.colors.onSurface)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(stringResource(id = R.string.cancel), color = Theme.colors.onSurface)
                }
            }
        }
    }

}

@Composable
fun AutoCompleteSelectBar(
    titleSelector: String = "",
    entries: List<Pair<String, Long>>,
    currentEntry: String = "",
    updateCurrentEntry: (String, Long?) -> Unit,
) {
    var itemElement by remember { mutableStateOf(currentEntry) }
    val heightTextFields by remember { mutableStateOf(55.dp) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {

        Text(
            text = titleSelector,
            style = MTextStyle.noteContent,
            color = Theme.colors.onSurface,
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightTextFields)
                    .border(
                        width = 1.dp,
                        color = Theme.colors.colorBorder,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                value = itemElement,
                onValueChange = {
                    itemElement = it
                    updateCurrentEntry(itemElement, null)
                    expanded = true
                },
                // Perform action when the TextField is clicked
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                if (interaction is PressInteraction.Release) {
                                    expanded = !expanded
                                }
                            }
                        }
                    },
                //placeholder = { Text(text = stringResource(id = R.string.place_null)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = Theme.colors.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    color = Theme.colors.onSurface,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "arrow",
                        tint = Color.Black
                    )
                }
            )

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .width(textFieldSize.width.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                    ) {

                        if (itemElement.isNotEmpty()) {
                            items(
                                entries.filter {
                                    it.first.lowercase()
                                        .contains(itemElement.lowercase())
                                }.sortedBy { it.first }
                            ) {
                                ItemElement(title = it.first) { title ->
                                    itemElement = title
                                    expanded = false
                                    updateCurrentEntry(title, it.second)
                                }
                            }
                        } else {
                            items(
                                entries.sortedBy { it.first }
                            ) {
                                ItemElement(title = it.first) { title ->
                                    itemElement = title
                                    expanded = false
                                    updateCurrentEntry(title, it.second)
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}

@Composable
fun ItemElement(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(vertical = 12.dp, horizontal = 15.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }
}

