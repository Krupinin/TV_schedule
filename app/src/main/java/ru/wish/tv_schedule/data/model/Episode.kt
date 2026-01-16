package ru.wish.tv_schedule.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "episodes")
data class Episode(
    @PrimaryKey val id: Int,
    val url: String?,
    val name: String?,
    val season: Int?,
    val number: Int?,
    val airdate: String?,
    val airtime: String?,
    val airstamp: String?,
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

@Entity(tableName = "shows")
data class Show(
    @PrimaryKey val id: Int,
    val url: String?,
    val name: String?,
    val type: String?,
    val language: String?,
    val genres: List<String>?,
    val status: String?,
    val runtime: Int?,
    val averageRuntime: Int?,
    val premiered: String?,
    val ended: String?,
    val officialSite: String?,
    val schedule: Schedule?,
    val rating: Rating?,
    val weight: Int?,
    val network: Network?,
    val webChannel: Any?, // Can be null or object
    val dvdCountry: Any?,
    val externals: Externals?,
    @SerializedName("image") val image: Image?,
    val summary: String?,
    val updated: Long?,
    val _links: Links?
)

data class Schedule(
    val time: String?,
    val days: List<String>?
)

data class Rating(
    val average: Double?
)

data class Network(
    val id: Int?,
    val name: String?,
    val country: Country?,
    val officialSite: String?
)

data class Country(
    val name: String?,
    val code: String?,
    val timezone: String?
)

data class Externals(
    val tvrage: Int?,
    val thetvdb: Int?,
    val imdb: String?
)

data class Links(
    val self: Link?,
    val previousepisode: Link?,
    val nextepisode: Link?
)

data class Link(
    val href: String?
)
