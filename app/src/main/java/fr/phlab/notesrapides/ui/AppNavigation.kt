package fr.phlab.notesrapides.ui

import androidx.navigation.NavHostController
import fr.phlab.notesrapides.ui.AppScreens.HOME_SCREEN
import fr.phlab.notesrapides.ui.AppScreens.NOTE_DETAIL_SCREEN
import fr.phlab.notesrapides.ui.NoteDetailArgs.NOTE_ID_ARG


private object AppScreens {
    const val HOME_SCREEN = "HOME"
    const val NOTE_DETAIL_SCREEN = "NOTE_DETAIL"
}

object NoteDetailArgs {
    const val NOTE_ID_ARG = "noteID"
}

object AppDestination {
    const val NOTE_DETAIL_ROUTE = "$NOTE_DETAIL_SCREEN?$NOTE_ID_ARG={$NOTE_ID_ARG}"
    const val HOME_ROUTE = HOME_SCREEN
}

class AppNavigationActions(private val navController: NavHostController) {
    fun navigateToNoteDetail(noteId: Long?) {
        navController.navigate(NOTE_DETAIL_SCREEN.let {
            if (noteId != null) "$it?$NOTE_ID_ARG=$noteId" else it
        })

    }

    fun navigateToHome() {
        navController.navigate(HOME_SCREEN)
    }

}
