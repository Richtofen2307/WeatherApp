package com.example.katas12

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) {


    suspend fun getCurrentWeather(location: String, apiKey: String, units: String, language: String): WeatherEntity {

        val weatherResponse = try {
            weatherApi.getCurrentWeather(location, apiKey, units, language)
        } catch (e: Exception) {
            null
        }

        return if (weatherResponse != null) {
            val weatherEntity = WeatherEntity(
                city = weatherResponse.name,
                temperature = "${weatherResponse.main.temp}°C",
                description = weatherResponse.weather.first().description
            )
            weatherDao.insertWeather(weatherEntity)
            weatherEntity
        } else {
            weatherDao.getWeatherByCity(location) ?: throw Exception("No weather data available")
        }
    }

    suspend fun getForecastWeather() = weatherApi.getForecastWeather("7fc58d6316862a2bcabc5bff628978c6", "metric", "es")
}