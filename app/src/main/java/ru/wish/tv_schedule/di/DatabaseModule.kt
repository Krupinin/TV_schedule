package ru.wish.tv_schedule.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.wish.tv_schedule.data.local.AppDatabase
import ru.wish.tv_schedule.data.local.EpisodeDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tv_schedule_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEpisodeDao(database: AppDatabase): EpisodeDao {
        return database.episodeDao()
    }
}
