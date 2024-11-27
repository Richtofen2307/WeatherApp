package com.example.katas12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    private val _currentWeather = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val currentWeather: StateFlow<WeatherUiState> = _currentWeather

    private val _forecast = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val forecast: StateFlow<ForecastUiState> = _forecast


    fun fetchCurrentWeather() {
        viewModelScope.launch {
            _currentWeather.value = WeatherUiState.Loading
            try {

                val weatherResponse = weatherRepository.getCurrentWeather()
                _currentWeather.value = WeatherUiState.Success(
                    city = weatherResponse.name,
                    temperature = "${weatherResponse.main.temp}°C",
                    description = weatherResponse.weather.first().description
                )
            } catch (e: Exception) {
                _currentWeather.value = WeatherUiState.Error("Error al obtener datos del clima")
            }
        }
    }
    fun fetchForecastWeather() {
        viewModelScope.launch {
            _forecast.value = ForecastUiState.Loading
            try {
                val forecastResponse = weatherRepository.getForecastWeather()
                _forecast.value = ForecastUiState.Success(forecastResponse.list)
            } catch (e: Exception) {
                _forecast.value = ForecastUiState.Error("Error al obtener el pronóstico")
            }
        }
    }

}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val city: String, val temperature: String, val description: String) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
sealed class ForecastUiState {
    object Loading : ForecastUiState()
    data class Success(val forecastList: List<ForecastItem>) : ForecastUiState()
    data class Error(val message: String) : ForecastUiState()
}
