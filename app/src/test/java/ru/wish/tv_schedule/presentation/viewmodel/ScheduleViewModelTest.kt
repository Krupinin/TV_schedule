package ru.wish.tv_schedule.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.usecase.GetScheduleUseCase
import ru.wish.tv_schedule.domain.util.Resource

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ScheduleViewModel
    private val getScheduleUseCase: GetScheduleUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ScheduleViewModel(getScheduleUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getSchedule_should_emit_Loading_then_Success_when_use_case_returns_success() = runTest {
        val country = "us"
        val date = "2023-10-01"
        val episodes = listOf(Episode(
            id = 1, name = "Test Episode",
            url = null,
            number = null,
            airdate = null,
            airtime = null,
            runtime = null,
            image = null,
            summary = null,
            show = null,
            country = null
        ))
        val successResource = Resource.Success(episodes)
        coEvery { getScheduleUseCase(country, date) } returns successResource

        viewModel.getSchedule(country, date)

        val initialState = viewModel.scheduleState.first()
        assertTrue(initialState is Resource.Loading)

        delay(100)
        val finalState = viewModel.scheduleState.first()
        assertTrue(finalState is Resource.Success)
        assertEquals(episodes, (finalState as Resource.Success).data)
    }

    @Test
    fun getSchedule_should_emit_Loading_then_Error_when_use_case_returns_error() = runTest {
        val country = "us"
        val date = "2023-10-01"
        val errorMessage = "Network error"
        val errorResource = Resource.Error<List<Episode>>(errorMessage)
        coEvery { getScheduleUseCase(country, date) } returns errorResource

        viewModel.getSchedule(country, date)

        val initialState = viewModel.scheduleState.first()
        assertTrue(initialState is Resource.Loading)

        delay(100)
        val finalState = viewModel.scheduleState.first()
        assertTrue(finalState is Resource.Error)
        assertEquals(errorMessage, (finalState as Resource.Error).message)
    }
}
