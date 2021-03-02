package com.uc3m.dresser.model

import com.google.gson.annotations.SerializedName


data class Clima(
    @SerializedName("coord")
    var coord: Coord,
    @SerializedName("weather")
    var weather: Weather,
    var base: String,
    @SerializedName("main")
    var main: Main,
    var visibility: Float,
    @SerializedName("wind")
    var wind: Wind,
    @SerializedName("clouds")
    var clouds: Clouds,
    var dt: Double,
    @SerializedName("sys")
    var sys: Sys,
    var timezone: Long,
    var id: Int,
    var name: String,
    var cod: Int
)
data class Coord(
    var lon: Float,
    var lat: Float,
)

data class Weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)

data class Main(
    var temp: Float,
    var feels_like: Float,
    var temp_min: Float,
    var temp_max: Float,
    var pressure: Float,
    var humidity: Float
)

data class Wind(
    var speed: Float,
    var deg: Float
)

data class Clouds(
    var all: Float
)

data class Sys(
    var type: Int,
    var id: Float,
    var message: Float,
    var country: String,
    var sunrise: Long,
    var sunset: Long
)
