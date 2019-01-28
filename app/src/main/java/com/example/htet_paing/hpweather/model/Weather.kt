package com.example.htet_paing.hpweather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "icon")val weatherIcon: String,
    @Json(name = "description") val weatherDesc: String
)