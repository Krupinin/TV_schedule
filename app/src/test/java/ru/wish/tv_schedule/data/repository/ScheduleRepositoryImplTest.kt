package ru.wish.tv_schedule.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.wish.tv_schedule.data.local.EpisodeDao
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.data.remote.TVMazeApi
import ru.wish.tv_schedule.domain.util.Resource
import java.io.IOException

class ScheduleRepositoryImplTest {

    private val api: TVMazeApi = mockk()
    private val dao: EpisodeDao = mockk()
    private val repository = ScheduleRepositoryImpl(api, dao)

    @Test
    fun getSchedule_should_return_success_when_API_call_succeeds_and_save_to_DB() = runTest {
        val country = "us"
        val date = "2023-10-01"
        val episodesFromApi = listOf(Episode(
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
        val episodesWithCountry = episodesFromApi.map { it.copy(country = country) }
        coEvery { api.getSchedule(country, date) } returns episodesFromApi
        coEvery { dao.insertEpisodes(episodesWithCountry) } returns Unit

        val result = repository.getSchedule(country, date)

        assertTrue(result is Resource.Success)
        assertEquals(episodesWithCountry, (result as Resource.Success).data)
        coVerify { api.getSchedule(country, date) }
        coVerify { dao.insertEpisodes(episodesWithCountry) }
    }

    @Test
    fun getSchedule_should_return_cached_data_when_network_fails_and_cache_exists() = runTest {
        val country = "us"
        val date = "2023-10-01"
        val cachedEpisodes = listOf(Episode(
            id = 1, name = "Cached Episode", country = country,
            url = null,
            number = null,
            airdate = null,
            airtime = null,
            runtime = null,
            image = null,
            summary = null,
            show = null
        ))
        coEvery { api.getSchedule(country, date) } throws IOException("Network error")
        coEvery { dao.getEpisodesByCountryAndDate(country, date) } returns cachedEpisodes

        val result = repository.getSchedule(country, date)

        assertTrue(result is Resource.Success)
        assertEquals(cachedEpisodes, (result as Resource.Success).data)
        coVerify { api.getSchedule(country, date) }
        coVerify { dao.getEpisodesByCountryAndDate(country, date) }
    }

    @Test
    fun getSchedule_should_return_error_when_network_fails_and_no_cache() = runTest {
        val country = "us"
        val date = "2023-10-01"
        coEvery { api.getSchedule(country, date) } throws IOException("Network error")
        coEvery { dao.getEpisodesByCountryAndDate(country, date) } returns emptyList()

        val result = repository.getSchedule(country, date)

        assertTrue(result is Resource.Error)
        assertEquals("Network error and no cached data", (result as Resource.Error).message)
    }

    @Test
    fun getSchedule_should_return_error_for_other_exceptions() = runTest {
        val country = "us"
        val date = "2023-10-01"
        val exceptionMessage = "Some other error"
        coEvery { api.getSchedule(country, date) } throws Exception(exceptionMessage)

        val result = repository.getSchedule(country, date)

        assertTrue(result is Resource.Error)
        assertEquals(exceptionMessage, (result as Resource.Error).message)
    }
}
