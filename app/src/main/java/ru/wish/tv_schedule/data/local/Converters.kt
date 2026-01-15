package ru.wish.tv_schedule.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromImage(value: ru.wish.tv_schedule.data.model.Image?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toImage(value: String?): ru.wish.tv_schedule.data.model.Image? {
        return if (value.isNullOrEmpty()) null else {
            gson.fromJson(value, ru.wish.tv_schedule.data.model.Image::class.java)
        }
    }

    @TypeConverter
    fun fromShow(value: ru.wish.tv_schedule.data.model.Show?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toShow(value: String?): ru.wish.tv_schedule.data.model.Show? {
        return if (value.isNullOrEmpty()) null else {
            gson.fromJson(value, ru.wish.tv_schedule.data.model.Show::class.java)
        }
    }
}
