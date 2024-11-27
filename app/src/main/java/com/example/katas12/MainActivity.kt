@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.katas12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.katas12.ui.theme.Katas12Theme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Katas12Theme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
        }
    }
}
}


@Composable
fun CurrentWeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherState by viewModel.currentWeather.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentWeather()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Clima Actual") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (weatherState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is WeatherUiState.Success -> {
                    val successState = weatherState as WeatherUiState.Success
                    Text(text = successState.city, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = successState.temperature, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = successState.description, style = MaterialTheme.typography.bodyMedium)
                }
                is WeatherUiState.Error -> {
                    val errorState = weatherState as WeatherUiState.Error
                    Text(text = "Error: ${errorState.message}")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCurrentWeatherScreen() {
    CurrentWeatherScreen()
}

@Composable
fun ForecastWeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val forecastState by viewModel.forecast.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchForecastWeather()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Pronóstico del Clima") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (forecastState) {
                is ForecastUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ForecastUiState.Success -> {
                    val successState = forecastState as ForecastUiState.Success
                    val forecastList = successState.forecastList

                    HorizontalPager(
                        count = forecastList.size,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) { page ->
                        val forecast = forecastList[page]
                        ForecastItemView(forecast)
                    }
                }
                is ForecastUiState.Error -> {
                    val errorState = forecastState as ForecastUiState.Error
                    Text(text = "Error: ${errorState.message}")
                }
            }
        }
    }
}

@Composable
fun ForecastItemView(forecast: ForecastItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Fecha: ${forecast.dt_txt}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Temperatura: ${forecast.main.temp}°C", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Descripción: ${forecast.weather.first().description}", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = "current_weather") {
        composable("current_weather") {
            CurrentWeatherScreen(viewModel = weatherViewModel)
        }
        composable("forecast_weather") {
            ForecastWeatherScreen(viewModel = weatherViewModel)
        }

        }

    }

