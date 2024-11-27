package com.example.katas12

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): CurrentWeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): ForecastResponse
}