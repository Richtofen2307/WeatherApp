package com.example.katas12.data.model

data class CurrentWeatherResponse(

    val main: CurrentWeatherMain,
    val weather: List<CurrentWeatherWeather>,
    val name: String
)

data class CurrentWeatherMain(
    val temp: Double,
    val humidity: Int
)

data class CurrentWeatherWeather(
    val description: String,
    val icon: String
)

