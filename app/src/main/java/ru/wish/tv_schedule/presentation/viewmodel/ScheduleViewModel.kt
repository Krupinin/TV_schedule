package ru.wish.tv_schedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.wish.tv_schedule.data.model.Episode
import ru.wish.tv_schedule.domain.usecase.GetScheduleUseCase
import ru.wish.tv_schedule.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    private val _scheduleState = MutableStateFlow<Resource<List<Episode>>>(Resource.Loading())
    val scheduleState: StateFlow<Resource<List<Episode>>> = _scheduleState

    fun getSchedule(country: String, date: String) {
        viewModelScope.launch {
            _scheduleState.value = Resource.Loading()
            val result = getScheduleUseCase(country, date)
            _scheduleState.value = result
        }
    }
}
