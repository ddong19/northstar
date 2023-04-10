package edu.umich.aehill.reminiscetest


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.*
import org.json.JSONObject
import java.util.*


class WeatherApiViewModel : ViewModel() {
    private val _weatherDataStart = MutableLiveData<Pair<Double, Double>>()
    val weatherDataStart: LiveData<Pair<Double, Double>> = _weatherDataStart

    private val _weatherDataEnd = MutableLiveData<Pair<Double, Double>>()
    val weatherDataEnd: LiveData<Pair<Double, Double>> = _weatherDataEnd

    fun getWeatherData(destination: String?, startDate: String?, callback: () -> Unit) {
        val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=$destination&dt=$startDate"

        val client = OkHttpClient()

        // get weather data for the start date
        val requestStart = Request.Builder().url(url).build()
        client.newCall(requestStart).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonString = response.body?.string()
                val jsonObject = JSONObject(jsonString.toString())
                val avgTemp = jsonObject
                    .getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day")
                    .getDouble("avgtemp_f")
                val totalPrecip = jsonObject
                    .getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day")
                    .getDouble("totalprecip_in")
                _weatherDataStart.postValue(Pair(avgTemp, totalPrecip))
                callback()
            }

            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.e("WeatherApiCall", "API Response Failed")
                _weatherDataStart.postValue(Pair(0.0, 0.0))
                callback()
            }
        })
    }

    fun getWeatherDataEnd(destination: String?, startDate: String?, callback: () -> Unit) {
        val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=$destination&dt=$startDate"

        val client = OkHttpClient()

        // get weather data for the start date
        val requestStart = Request.Builder().url(url).build()
        client.newCall(requestStart).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonString = response.body?.string()
                val jsonObject = JSONObject(jsonString.toString())
                val avgTemp = jsonObject
                    .getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day")
                    .getDouble("avgtemp_f")
                val totalPrecip = jsonObject
                    .getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day")
                    .getDouble("totalprecip_in")
                _weatherDataEnd.postValue(Pair(avgTemp, totalPrecip))
                callback()
            }

            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.e("WeatherApiCall", "API Response Failed")
                _weatherDataEnd.postValue(Pair(0.0, 0.0))
                callback()
            }
        })
    }

}

@Composable
fun DisplayWeatherContent(context: Context, tripID: String?, destination: String?, startDate: String?, endDate: String?) {
    Log.e("GetWeather", "DISPLAYING WEATHER CONTENT")
        val startDateDisplay = formatDateForDisplay(startDate)
        val endDateDisplay = formatDateForDisplay(endDate)

        val formattedStartDateApi = formatDateForApi(startDate)
        val formattedEndDateApi = formatDateForApi(endDate)
// Initializes a viewModel for obtaining weatherAPI information
        val weatherApiViewModel = viewModel<WeatherApiViewModel>()
        val weatherLiveDataStart = weatherApiViewModel.weatherDataStart
        val weatherLiveDataEnd = weatherApiViewModel.weatherDataEnd

//Gets our weather data from the API for each date in the range

    weatherApiViewModel.getWeatherData(destination, formattedStartDateApi) {
        weatherApiViewModel.getWeatherDataEnd(destination, formattedEndDateApi) {}
    }

        // Create a StateFlow to hold the weather data for the current date
        val weatherStateStart = remember { MutableStateFlow(Pair(0.0, 0.0)) }
        val weatherStateEnd = remember { MutableStateFlow(Pair(0.0, 0.0)) }
        // Collect the data from the LiveData and update the StateFlow for the current date
        LaunchedEffect(weatherLiveDataStart) {
            weatherLiveDataStart.observeForever {
                weatherStateStart.value = it ?: Pair(0.0, 0.0)
            }
        }
        LaunchedEffect(weatherLiveDataEnd) {
            weatherLiveDataEnd.observeForever {
                weatherStateEnd.value = it ?: Pair(0.0, 0.0)
            }
        }

        // Read the weather data from the StateFlow for the current date
        val weatherDataStart = weatherStateStart.collectAsState().value
        val weatherDataEnd = weatherStateEnd.collectAsState().value
        Log.e("DisplayWeatherContent START", weatherDataStart.toString())
        Log.e("DisplayWeatherContent END", weatherDataEnd.toString())
        // Add the weather data for the current date to the list
//        weatherDataList.add(weatherData)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display image based on weather conditions
            val (startTemp, startPrecip) = weatherDataStart
            val (endTemp, endPrecip) = weatherDataEnd

            val tempConditionStart = when {
                startTemp > 70 -> "warm"
                startTemp < 70 && startTemp > 45 -> "moderate"
                else -> "cold"
            }

            val precipConditionStart = when {
                startPrecip < 0.2 -> "clear"
                startPrecip > 0.2 && startPrecip < 0.5 -> "moderate_rain"
                startPrecip > 0.5 -> "rainy"
                else -> "moderate_rain"
            }

            val tempConditionEnd = when {
                endTemp > 70 -> "warm"
                endTemp < 70 && endTemp > 45 -> "moderate"
                else -> "cold"
            }

            val precipConditionEnd = when {
                endPrecip < 0.2 -> "clear"
                endPrecip > 0.2 && endPrecip < 0.5 -> "moderate_rain"
                endPrecip > 0.5 -> "rainy"
                else -> "moderate_rain"
            }

            val imageResourceTempStart = when (tempConditionStart) {
                "warm" -> R.drawable.sunny
                "moderate" -> R.drawable.moderate_temp
                "cold" -> R.drawable.cold
                else -> R.drawable.moderate_temp
            }
            val imageResourcePrecipStart = when (precipConditionStart) {
                "clear" -> R.drawable.no_rain
                "moderate_rain" -> R.drawable.moderate_rain
                "rainy" -> R.drawable.rainy
                else -> R.drawable.moderate_temp
            }
            val imageResourceTempEnd = when (tempConditionEnd) {
                "warm" -> R.drawable.sunny
                "moderate" -> R.drawable.moderate_temp
                "cold" -> R.drawable.cold
                else -> R.drawable.moderate_temp
            }
            val imageResourcePrecipEnd= when (precipConditionEnd) {
                "clear" -> R.drawable.no_rain
                "moderate_rain" -> R.drawable.moderate_rain
                "rainy" -> R.drawable.rainy
                else -> R.drawable.moderate_temp
            }

            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(2.dp)
            ) {
                Text(
                    text = "Trip Date Range: ",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .padding(2.dp)
            ) {
                Text(
                    text = "$startDateDisplay - $endDateDisplay",
                    fontSize = 28.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(2.dp)
            ) {
                Text(
                    text = "Start Date Temperature: ",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .padding(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5F)
                ) {
                    Text(
                        text = "${weatherDataStart.first} °F",
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(id = imageResourceTempStart),
                        contentDescription = "Weather image",
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 16.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(2.dp)
            ){
                Text(
                    text = "Start Date Precipitation: ",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .padding(2.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5F)
                ) {
                    Text(
                        text = "${weatherDataStart.second} in.",
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(id = imageResourcePrecipStart),
                        contentDescription = "Weather image",
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 16.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(2.dp)
            ){
                Text(
                    text = "End Date Temperature: ",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .padding(2.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5F)
                ) {
                    Text(
                        text = "${weatherDataEnd.first} °F",
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(id = imageResourceTempEnd),
                        contentDescription = "Weather image",
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 16.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(2.dp)
            ){
                Text(
                    text = "End Date Precipitation: ",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .padding(2.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5F)
                ) {
                    Text(
                        text = "${weatherDataEnd.second} in.",
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(id = imageResourcePrecipEnd),
                        contentDescription = "Weather image",
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 16.dp)
                    )
                }
            }
        }

    }

}


////Helper function that generates a list of localDates between start date and end date
//fun generateDates(startDate: String?, endDate: String?): List<LocalDate> {
//    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//    var date = LocalDate.parse(startDate, formatter)
//    val endDateParsed = LocalDate.parse(endDate, formatter)
//    val dates = mutableListOf<LocalDate>()
//
//    while (!date.isAfter(endDateParsed)) {
//        dates.add(date)
//        date = date.plusDays(1)
//    }
//
//    return dates
//}

fun formatDateForDisplay(inputDate: String?): String {
    val month = inputDate?.substring(0, 2)
    val day = inputDate?.substring(2, 4)
    val year = inputDate?.substring(4)
    return "$month/$day/$year"
}

fun formatDateForApi(inputDate: String?): String {
    val month = inputDate?.substring(0, 2)
    val day = inputDate?.substring(2, 4)
    val year = inputDate?.substring(4)
    return "$year-$month-$day"
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeatherView(
    context: Context,
    navController: NavHostController,
    customModifier: Modifier,
    tripID: String?,
    destination: String?,
    startDate: String?,
    endDate: String?
) {
    Scaffold(
        topBar = {
            // Your existing top bar code here
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                DisplayWeatherContent(context, tripID, destination, startDate, endDate)
            }
        }
    )
}


//fun WeatherView(context: Context, navController: NavHostController, customModifier: Modifier, tripID: String?, destination: String?, startDate: String?, endDate: String?) {
////    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
//    DisplayWeatherContent(context, tripID, destination, startDate, endDate)
////        FloatingActionButton(
////            backgroundColor = Color(0xFFFFC107),
////            contentColor = Color(0xFF00FF00),
////            modifier = Modifier.padding(310.dp, 520.dp, 8.dp, 8.dp),
////
////            onClick = {
////                navController.navigate("CompletedTripView/$tripID")
////            }
////        ) {
////            Icon(Icons.Default.ArrowBack, "back")
////        }
////    })
//}
