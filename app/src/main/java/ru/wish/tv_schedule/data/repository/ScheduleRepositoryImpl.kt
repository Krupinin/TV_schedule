package ru.wish.tv_schedule.data.repository

import ru.wish.tv_schedule.data.local.EpisodeDao
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.data.remote.TVMazeApi
import ru.wish.tv_schedule.domain.repository.ScheduleRepository
import ru.wish.tv_schedule.domain.util.Resource
import java.io.IOException
import javax.inject.Inject

// реализация интерфейса ScheduleRepository (domain/repository/Sch....)
class ScheduleRepositoryImpl @Inject constructor(
    private val api: TVMazeApi,
    private val dao: EpisodeDao
) : ScheduleRepository {

    override suspend fun getSchedule(country: String, date: String): Resource<List<Episode>> {
        return try {

//            val cached = dao.getEpisodesByCountryAndDate(country, date)
//            if (cached.isNotEmpty()) {
//                return Resource.Success(cached)
//            }

            // дергаем API
            val response = api.getSchedule(country, date)
            // Добавляем страну к каждому эпизоду
            val episodesWithCountry = response.map { it.copy(country = country) }
            dao.insertEpisodes(episodesWithCountry)
            Resource.Success(episodesWithCountry)
        } catch (_: IOException) {
            // Network error
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
