package ru.wish.tv_schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.wish.tv_schedule.presentation.screen.InputScreen
import ru.wish.tv_schedule.presentation.screen.ScheduleScreen
import ru.wish.tv_schedule.ui.theme.TV_scheduleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TV_scheduleTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "input") {
                    composable("input") { InputScreen(navController) }
                    composable("schedule/{country}/{date}") { backStackEntry ->
                        val country = backStackEntry.arguments?.getString("country") ?: ""
                        val date = backStackEntry.arguments?.getString("date") ?: ""
                        ScheduleScreen(navController, country, date)
                    }
                }
            }
        }
    }
}
