package ru.wish.tv_schedule.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "episodes")
data class Episode(
    @PrimaryKey val id: Int,
    val url: String?,
    val name: String?,
    val number: Int?,
    val airdate: String?,
    val airtime: String?,
    val runtime: Int?,
    @SerializedName("image") val image: Image?,
    val summary: String?,
    val show: Show?,
    val country: String?
)

data class Image(
    val medium: String?,
    val original: String?
)

data class Show(
    val name: String?
)
