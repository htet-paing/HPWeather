package com.example.htet_paing.hpweather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "weather") val weatherList: List<Weather>,
    @Json(name = "main") val main: Main,
    @Json(name = "name") val cityName: String

)