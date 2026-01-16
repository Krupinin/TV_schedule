package ru.wish.tv_schedule.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.wish.tv_schedule.data.repository.ScheduleRepositoryImpl
import ru.wish.tv_schedule.domain.repository.ScheduleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds //используется для связывания интерфейсов с реализациями, без необходимости писать код предоставления.
    @Singleton
    abstract fun bindScheduleRepository(impl: ScheduleRepositoryImpl): ScheduleRepository
}
