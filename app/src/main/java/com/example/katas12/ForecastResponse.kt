package com.example.katas12

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String,
    val icon: String
)
