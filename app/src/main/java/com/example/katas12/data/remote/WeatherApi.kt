package com.example.katas12.data.remote

import com.example.katas12.data.model.ForecastResponse
import com.example.katas12.data.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): ForecastResponse

}