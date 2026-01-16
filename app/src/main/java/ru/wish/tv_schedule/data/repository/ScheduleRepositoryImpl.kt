package ru.wish.tv_schedule.data.repository

import ru.wish.tv_schedule.data.local.EpisodeDao
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.data.remote.TVMazeApi
import ru.wish.tv_schedule.domain.repository.ScheduleRepository
import ru.wish.tv_schedule.domain.util.Resource
import java.io.IOException
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val api: TVMazeApi,
    private val dao: EpisodeDao
) : ScheduleRepository {

    override suspend fun getSchedule(country: String, date: String): Resource<List<Episode>> {
        return try {
            // First, try to get from cache
            val cached = dao.getEpisodesByCountryAndDate(country, date)
            if (cached.isNotEmpty()) {
                return Resource.Success(cached)
            }

            // If no cache, fetch from API
            val response = api.getSchedule(country, date)
            // Save to cache with country
            val episodesWithCountry = response.map { it.copy(country = country) }
            dao.insertEpisodes(episodesWithCountry)
            Resource.Success(episodesWithCountry)
        } catch (_: IOException) {
            // Network error, try cache
            val cached = dao.getEpisodesByCountryAndDate(country, date)
            if (cached.isNotEmpty()) {
                Resource.Success(cached)
            } else {
                Resource.Error("Network error and no cached data", null)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred", null)
        }
    }
}
