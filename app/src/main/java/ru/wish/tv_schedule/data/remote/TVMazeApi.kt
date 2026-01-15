package ru.wish.tv_schedule.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.wish.tv_schedule.data.model.Episode

interface TVMazeApi {

    @GET("schedule")
    suspend fun getSchedule(
        @Query("country") country: String,
        @Query("date") date: String
    ): List<Episode>

    companion object {
        const val BASE_URL = "https://api.tvmaze.com"
    }
}
