package ru.wish.tv_schedule.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.util.Resource
import ru.wish.tv_schedule.presentation.viewmodel.ScheduleViewModel

class ScheduleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createMockViewModel(state: Resource<List<Episode>>): ScheduleViewModel {
        val mockStateFlow = MutableStateFlow(state)
        return mockk<ScheduleViewModel> {
            every { scheduleState } returns mockStateFlow
            every { getSchedule(any(), any()) } returns Unit
        }
    }

    @Test
    fun scheduleScreen_displays_title_and_back_button() {
        val navController = mockk<NavController>()
        val viewModel = createMockViewModel(Resource.Loading())
        val country = "US"
        val date = "2023-10-01"

        composeTestRule.setContent {
            ScheduleScreen(navController = navController, country = country, date = date, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Schedule for US on 2023-10-01").assertIsDisplayed()
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()
    }

    @Test
    fun scheduleScreen_back_button_navigates_back() {
        val navController = mockk<NavController>(relaxed = true)
        val viewModel = createMockViewModel(Resource.Loading())
        val country = "US"
        val date = "2023-10-01"

        composeTestRule.setContent {
            ScheduleScreen(navController = navController, country = country, date = date, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Back").performClick()

        verify { navController.popBackStack() }
    }

    @Test
    fun scheduleScreen_displays_loading_state() {
        val navController = mockk<NavController>()
        val viewModel = createMockViewModel(Resource.Loading())
        val country = "US"
        val date = "2023-10-01"

        composeTestRule.setContent {
            ScheduleScreen(navController = navController, country = country, date = date, viewModel = viewModel)
        }

        // тк загрузочный кружочек не содерджит текста, мы можем проверить, что ошибки или эпизодов не выводится
        composeTestRule.onNodeWithText("Error:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Episode name:").assertDoesNotExist()
    }

    @Test
    fun scheduleScreen_displays_error_state() {
        val navController = mockk<NavController>()
        val errorMessage = "Network error"
        val viewModel = createMockViewModel(Resource.Error(errorMessage))
        val country = "US"
        val date = "2023-10-01"

        composeTestRule.setContent {
            ScheduleScreen(navController = navController, country = country, date = date, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Error: $errorMessage").assertIsDisplayed()
    }

    @Test
    fun scheduleScreen_displays_success_state_with_episodes() {
        val navController = mockk<NavController>()
        val episodes = listOf(
            Episode(
                id = 1,
                name = "Pilot",
                url = "https://example.com/episode1",
                number = 1,
                airdate = "2023-10-01",
                airtime = "20:00",
                runtime = 60,
                image = null,
                summary = "A great episode",
                show = null,
                country = "US"
            )
        )
        val viewModel = createMockViewModel(Resource.Success(episodes))
        val country = "US"
        val date = "2023-10-01"

        composeTestRule.setContent {
            ScheduleScreen(navController = navController, country = country, date = date, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Episode name: Pilot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Episode number: 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Airtime: 20:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Runtime: 60 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Summary: A great episode").assertIsDisplayed()
    }

    @Test
    fun scheduleScreen_displays_multiple_episodes() {
        val navController = mockk<NavController>()
        val episodes = listOf(
            Episode(
                id = 1,
                name = "Pilot",
                url = null,
                number = 1,
                airdate = "2023-10-01",
                airtime = "20:00",
                runtime = 30,
                image = null,
                summary = "First episode",
                show = null,
                country = "US"
            ),
            Episode(
                id = 2,
                name = "Episode 2",
                url = "https://example.com/episode2",
                number = 2,
                airdate = "2023-10-02",
                airtime = "20:30",
                runtime = 45,
                image = null,
                summary = "Second episode",
                show = null,
                country = "US"
            )
        )
        val viewModel = createMockViewModel(Resource.Success(episodes))
        val country = "US"
        val date = "2023-10-01"

        composeTestRule.setContent {
            ScheduleScreen(navController = navController, country = country, date = date, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Episode name: Pilot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Episode name: Episode 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Runtime: 30 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Runtime: 45 min").assertIsDisplayed()
    }
}
