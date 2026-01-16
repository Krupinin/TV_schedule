package ru.wish.tv_schedule.domain.usecase

import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.repository.ScheduleRepository
import ru.wish.tv_schedule.domain.util.Resource
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {

    // operator позволяет вызывать use case как функцию: useCase(country, date)
    suspend operator fun invoke(country: String, date: String): Resource<List<Episode>> {
        return repository.getSchedule(country, date)
    }
}
