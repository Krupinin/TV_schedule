package ru.wish.tv_schedule.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import android.content.Intent
import androidx.compose.foundation.clickable
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.util.Resource
import ru.wish.tv_schedule.presentation.viewmodel.ScheduleViewModel
import androidx.core.net.toUri

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
            Text("Schedule for $country on $date", style = MaterialTheme.typography.titleLarge)
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
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            episode.image?.medium?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Episode image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)
                )
            }
            Text("Show: ${episode.show?.name}", style = MaterialTheme.typography.titleMedium)
            Text("Episode name: ${episode.name}", style = MaterialTheme.typography.titleMedium)
            Text("Episode number: ${episode.number}", style = MaterialTheme.typography.bodySmall)
            Text("Airtime: ${episode.airtime}", style = MaterialTheme.typography.bodySmall)
            Text("Runtime: ${episode.runtime} min", style = MaterialTheme.typography.bodySmall)
            episode.url?.let { url ->
                Text(
                    text = "Link: $url",
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                    }
                )
            }
            episode.summary?.let {
                Text("Summary: ${it.replace(Regex("<[^>]*>"), "")}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
