package com.example.katas12.data.repository

import com.example.katas12.data.model.ForecastItem
import com.example.katas12.data.model.Main
import com.example.katas12.data.remote.WeatherApi
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
                description = weatherResponse.weather.first().description,

                )
            weatherDao.insertWeather(weatherEntity)
            weatherEntity
        } else {
            weatherDao.getWeatherByCity(location) ?: throw Exception("No weather data available")
        }
    }

    suspend fun getForecastWeather() = weatherApi.getForecastWeather(city ="Medellin", "7fc58d6316862a2bcabc5bff628978c6", "metric", "es")

    suspend fun getDailyForecast(location: String, apiKey: String, units: String, lang: String): List<ForecastItem> {
        val forecastResponse = weatherApi.getForecastWeather(location, apiKey, units, lang)

        return forecastResponse.list.groupBy { it.dt_txt.split(" ").first() }
            .map { (date, forecasts) ->
                val averageTemp = forecasts.map { it.main.temp }.average()
                val mainWeather = forecasts.first().weather.firstOrNull()
                ForecastItem(
                    dt_txt = date,
                    main = Main(temp = averageTemp),
                    weather = listOfNotNull(mainWeather)
                )
            }
    }

}