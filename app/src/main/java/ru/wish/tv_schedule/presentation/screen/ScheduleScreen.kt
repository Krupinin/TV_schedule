package ru.wish.tv_schedule.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.util.Resource
import ru.wish.tv_schedule.presentation.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    navController: NavController,
    country: String,
    date: String,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val scheduleState by viewModel.scheduleState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSchedule(country, date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Schedule for $country on $date", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = scheduleState) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                LazyColumn {
                    items(state.data ?: emptyList()) { episode ->
                        EpisodeItem(episode)
                    }
                }
            }
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}")
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(episode: Episode) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Episode ${episode.number}: ${episode.name}", style = MaterialTheme.typography.titleMedium)
            Text("Show: ${episode.show?.name}", style = MaterialTheme.typography.bodyMedium)
            Text("Airtime: ${episode.airtime}", style = MaterialTheme.typography.bodySmall)
            Text("Runtime: ${episode.runtime} min", style = MaterialTheme.typography.bodySmall)
            episode.summary?.let {
                Text("Summary: ${it.replace(Regex("<[^>]*>"), "")}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
