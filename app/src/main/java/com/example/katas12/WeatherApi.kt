package com.example.katas12

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

    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("q") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): ForecastResponse
}