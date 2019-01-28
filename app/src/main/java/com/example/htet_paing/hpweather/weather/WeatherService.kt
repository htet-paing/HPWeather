package com.example.htet_paing.hpweather.weather

import com.example.htet_paing.hpweather.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{
    @GET("weather")
    fun getWeatherByCoordinates(@Query("lat") latitude: Double,
                                @Query("lon") longitude: Double,
                                @Query("appid") apiKey: String,
                                @Query("units") unit: String
    ):Call<WeatherResponse>

    @GET("weather")
    fun getWeatherByCityName(@Query("q") cityName: String,
                             @Query("appid") apiKey: String,
                             @Query("units") units: String
    ):Call<WeatherResponse>
}