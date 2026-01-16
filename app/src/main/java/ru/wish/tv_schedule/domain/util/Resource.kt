package ru.wish.tv_schedule.domain.util

// T — это параметр типа (type parameter) в обобщённом (generic) (может быть любым типом данных)
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
