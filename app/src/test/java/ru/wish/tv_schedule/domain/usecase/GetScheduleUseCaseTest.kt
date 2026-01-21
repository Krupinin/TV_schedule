package ru.wish.tv_schedule.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.repository.ScheduleRepository
import ru.wish.tv_schedule.domain.util.Resource

class GetScheduleUseCaseTest {
    private val repository: ScheduleRepository = mockk()
    private val useCase = GetScheduleUseCase(repository)

    @Test
    fun invoke_should_call_repository_getSchedule_and_return_result() = runTest {
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
        val expectedResource = Resource.Success(episodes)
        coEvery { repository.getSchedule(country, date) } returns expectedResource

        val result = useCase(country, date)

        assertEquals(expectedResource, result)
        coVerify { repository.getSchedule(country, date) }
    }
}
