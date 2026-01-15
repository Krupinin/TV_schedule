package ru.wish.tv_schedule.domain.repository

import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.util.Resource

interface ScheduleRepository {

    suspend fun getSchedule(country: String, date: String): Resource<List<Episode>>
}
