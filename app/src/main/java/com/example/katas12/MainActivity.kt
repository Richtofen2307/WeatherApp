@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.katas12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.rememberScrollState
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
import coil.compose.*
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint
import com.example.katas12.*
import com.example.katas12.ui.*



@AndroidEntryPoint
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
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "current_weather") {
        composable("current_weather") {
            CurrentWeatherScreen(navController = navController)
        }
        composable("forecast_weather") {
            ForecastWeatherScreen(navController = navController)
        }
    }
}


@Composable
fun CurrentWeatherScreen(navController: NavHostController, viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherState by viewModel.currentWeather.collectAsState()
    val hourlyForecastState by viewModel.hourlyForecast.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentWeather("Medellin")
        viewModel.fetchHourlyForecast("Medellin")
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
            verticalArrangement = Arrangement.Top, // Ajustar para mostrar los elementos al principio
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
                    Text(
                        text = successState.temperature,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = successState.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is WeatherUiState.Error -> {
                    val errorState = weatherState as WeatherUiState.Error
                    Text(text = "Error: ${errorState.message}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el pronóstico horario si está disponible
            when (hourlyForecastState) {
                is ForecastUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ForecastUiState.Success -> {
                    val forecastList = (hourlyForecastState as ForecastUiState.Success).forecastList
                    LazyRow {
                        items(forecastList) { forecast ->
                            ForecastItemView(forecast) // Card para cada pronóstico horario
                        }
                    }
                }

                is ForecastUiState.Error -> {
                    val errorState = hourlyForecastState as ForecastUiState.Error
                    Text(text = "Error al cargar el pronóstico horario: ${errorState.message}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("forecast_weather") }) {
                Text("Ver Pronóstico")
            }
        }
    }
}

@Composable
fun ForecastWeatherScreen(
    navController: NavHostController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val forecastState by viewModel.forecast.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchForecastWeather()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Pronóstico del Clima") })
        }
    ) { paddingValues ->
        when (forecastState) {
            is ForecastUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ForecastUiState.Success -> {
                val successState = forecastState as ForecastUiState.Success
                val forecastList = successState.forecastList

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(forecastList) { forecast ->
                            ForecastItemView(forecast)
                        }
                    }
                }
            }

            is ForecastUiState.Error -> {
                val errorState = forecastState as ForecastUiState.Error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error: ${errorState.message}")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { navController.navigate("current_weather") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Volver a Clima Actual")
            }
        }
    }
}

@Composable
fun ForecastItemView(forecast: ForecastItem) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fecha y hora del pronóstico
            val dateTime = forecast.dt_txt.split(" ") // Esto divide la fecha y hora
            val date = dateTime.first() // Fecha
            val time = dateTime.getOrElse(1) { "Sin hora" } // Hora (con manejo de error si no existe)

            Text(
                text = "$date $time", // Muestra la fecha y hora
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Icono del clima
            val icon = forecast.weather.firstOrNull()?.icon
            if (icon != null) {
                Image(
                    painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${icon}@2x.png"),
                    contentDescription = "Icono del clima",
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Temperatura
            Text(
                text = "${forecast.main.temp}°C",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Descripción del clima
            val description = forecast.weather.firstOrNull()?.description ?: "Sin descripción"
            Text(
                text = description.capitalize(),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}




