package fr.phlab.notesrapides.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.phlab.notesrapides.ui.NoteDetailArgs.NOTE_ID_ARG
import fr.phlab.notesrapides.utils.style.ThemeProvider


@Composable
fun MainCompose(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.HOME_ROUTE,
    navActions: AppNavigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
) {

    ThemeProvider()
    {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(AppDestination.HOME_ROUTE) {
                HomeView(
                    onAddNote = { navActions.navigateToNoteDetail(null) },
                    onNoteClick = { note -> navActions.navigateToNoteDetail(note.id) },
                )
            }
            composable(
                AppDestination.NOTE_DETAIL_ROUTE,
                arguments = listOf(navArgument(NOTE_ID_ARG) { nullable = true })
            ) { entry ->
                val noteId = entry.arguments?.getString(NOTE_ID_ARG)
                NoteDetailView(
                    currentNoteId = noteId?.toLong(),
                    onBack = { navController.popBackStack() })
            }
        }

    }
}