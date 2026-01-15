package ru.wish.tv_schedule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.wish.tv_schedule.data.model.Episode

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<Episode>)

    @Query("SELECT * FROM episodes WHERE airdate = :date")
    suspend fun getEpisodesByDate(date: String): List<Episode>

    @Query("DELETE FROM episodes WHERE airdate = :date")
    suspend fun deleteEpisodesByDate(date: String)
}
