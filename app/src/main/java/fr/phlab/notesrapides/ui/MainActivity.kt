package fr.phlab.notesrapides.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //val vm: IntroViewModel = hiltViewModel()
            //val isOnboardedState = vm.isOnboarded.collectAsState()
            AppCompatTheme {
                MainCompose()
            }
        }

    }

}