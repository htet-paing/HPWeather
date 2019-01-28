package com.example.htet_paing.hpweather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp") val temperature: Double,
    @Json(name = "humidity") val humidity: Int
)