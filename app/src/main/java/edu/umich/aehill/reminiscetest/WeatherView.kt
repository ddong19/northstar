package edu.umich.aehill.reminiscetest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class WeatherApiViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<Pair<Double, Double>>()
    val weatherData: LiveData<Pair<Double, Double>> = _weatherData

    fun getWeatherData(destination: String?, date: String?) {
        val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=$destination&dt=$date"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonString = response.body?.string()
                val jsonObject = JSONObject(jsonString)
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
                _weatherData.postValue(Pair(avgTemp, totalPrecip))
            }

            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.e("WeatherApiCall", "API Response Failed")
                _weatherData.postValue(Pair(0.0, 0.0))
            }
        })
    }
}
@Composable
fun DisplayWeatherContent(context: Context, tripID: String?, destination: String?, startDate: String?, endDate: String?) {
    Log.e("GetWeather", "DISPLAYING WEATHER CONTENT")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Weather Statistics!",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        val startDateDisplay = formatDateForDisplay(startDate)
        val endDateDisplay = formatDateForDisplay(endDate)

        val formattedStartDateApi = formatDateForApi(startDate)
        val formattedEndDateApi = formatDateForApi(endDate)
        val dateRange = generateDates(formattedStartDateApi, formattedEndDateApi)
        Log.e("DATE RANGE", dateRange.toString())

// Initializes a viewModel for obtaining weatherAPI information
        val weatherApiViewModel = viewModel<WeatherApiViewModel>()
        val weatherLiveData = weatherApiViewModel.weatherData

// Create a list to hold the weather data for all days in the range
        val weatherDataList = mutableListOf<Pair<Double, Double>>()

//Gets our weather data from the API for each date in the range

        weatherApiViewModel.getWeatherData(destination, formattedStartDateApi)

        // Create a StateFlow to hold the weather data for the current date
        val weatherState = remember { MutableStateFlow(Pair(0.0, 0.0)) }

        // Collect the data from the LiveData and update the StateFlow for the current date
        LaunchedEffect(weatherLiveData) {
            weatherLiveData.observeForever {
                weatherState.value = it ?: Pair(0.0, 0.0)
            }
        }

        // Read the weather data from the StateFlow for the current date
        val weatherData = weatherState.collectAsState().value
        Log.e("DisplayWeatherContent", weatherData.toString())

        // Add the weather data for the current date to the list
        weatherDataList.add(weatherData)
        Text(
            text = "Trip Date Range: ",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "$startDateDisplay - $endDateDisplay",
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Temperature: ",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "${weatherData.first} °F",
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Precipitation: ",
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "${weatherData.second} in.",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
        val printedStartDate = formatDateForDisplay(startDate)
        val formattedStartDateApi = formatDateForApi(startDate)
        val formattedEndDateApi = formatDateForApi(endDate)
        val dateRange = generateDates(formattedStartDateApi, formattedEndDateApi)
        Log.e("DATE RANGE", dateRange.toString())

        // Initializes a viewModel for obtaining weatherAPI information
        val weatherApiViewModel = viewModel<WeatherApiViewModel>()
        val weatherLiveData = weatherApiViewModel.weatherData

        //Gets our weather data from the API
        weatherApiViewModel.getWeatherData(destination, dateRange[0].toString())

        // Create a StateFlow to hold the weather data
        val weatherState = remember { MutableStateFlow(Pair(0.0, 0.0)) }

        // Collect the data from the LiveData and update the StateFlow
        LaunchedEffect(weatherLiveData) {
            weatherLiveData.observeForever {
                weatherState.value = it ?: Pair(0.0, 0.0)
            }
        }

        // Read the weather data from the StateFlow
        val weatherData = weatherState.collectAsState().value
        Log.e("DisplayWeatherContent", weatherData.toString())

//        Text(
//            text = "Date: $printedStartDate",
//            fontSize = 20.sp,
//            color = Color.Black,
//            modifier = Modifier.padding(top = 16.dp)
//        )
//
//        Text(
//            text = "Temperature: ${weatherData.first} °F",
//            fontSize = 20.sp,
//            color = Color.Black,
//            modifier = Modifier.padding(top = 16.dp)
//        )
//
//        Text(
//            text = "Precipitation: ${weatherData.second} in.",
//            fontSize = 20.sp,
//            color = Color.Black,
//            modifier = Modifier.padding(top = 16.dp)
//        )
    }

fun generateDates(startDate: String?, endDate: String?): List<LocalDate> {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    var date = LocalDate.parse(startDate, formatter)
    val endDateParsed = LocalDate.parse(endDate, formatter)
    val dates = mutableListOf<LocalDate>()

    while (!date.isAfter(endDateParsed)) {
        dates.add(date)
        date = date.plusDays(1)
    }

    return dates
}

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

//@Composable
//fun DisplayWeatherContent(context: Context, tripID: String?, destination: String?, startDate: String?, endDate: String?) {
//    Log.e("GetWeather", "DISPLAYING WEATHER CONTENT")
//    Row(
//        modifier = Modifier.fillMaxSize(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = "Weather Statistics!",
//            modifier = Modifier
//                .padding(28.dp, 40.dp, 28.dp, 0.dp)
//                .fillMaxWidth(1f),
//            fontSize = 30.sp,
//            textAlign = TextAlign.Center,
//            color = Color.Black
//        )
//    }
//
//    val weatherApiViewModel = viewModel<WeatherApiViewModel>()
//    val weatherLiveData = weatherApiViewModel.weatherData
//
//    // Use remember to store the state of whether the API call has been made
//    val hasApiBeenCalled = remember { mutableStateOf(false) }
//
//    // Create a StateFlow to hold the weather data
//    val weatherState = remember { MutableStateFlow(Pair(0.0, 0.0)) }
//
//    // Only make the API call if it hasn't been made yet
//    if (!hasApiBeenCalled.value) {
//        weatherApiViewModel.getWeatherData("London", "2022-08-20")
//        hasApiBeenCalled.value = true
//    }
//
//    // Collect the data from the LiveData and update the StateFlow
//    LaunchedEffect(weatherLiveData) {
//        weatherLiveData.observeForever {
//            weatherState.value = it ?: Pair(0.0, 0.0)
//        }
//    }
//
//    // Read the weather data from the StateFlow
//    val weatherData = weatherState.collectAsState().value
//    Log.e("DisplayWeatherContent", weatherData.toString())
//    val avgTemp = weatherData.first.toString()
//    val totalPrecip = weatherData.second.toString()
//    Log.e("weatherData.first",weatherData.first.toString())
//    Log.e("weatherData.second",weatherData.second.toString())
//
//    Row(
//        modifier = Modifier.fillMaxSize(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = "Average Temperature: $avgTemp °F",
//            fontSize = 20.sp,
//            textAlign = TextAlign.Center,
//            color = Color.Black,
//            modifier = Modifier.padding(16.dp)
//        )
//        Text(
//            text = "Total Precipitation: $totalPrecip in",
//            fontSize = 20.sp,
//            textAlign = TextAlign.Center,
//            color = Color.Black,
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}
//



//@Composable // UI For displaying weather content
//fun DisplayWeatherContent(context: Context, tripID: String?, destination: String?, startDate: String?, endDate: String?) {
//    Log.e("GetWeather", "DISPLAYING WEATHER CONTENT")
//    Row(
//        modifier = Modifier.fillMaxSize(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = "Weather Statistics!",
//            modifier = Modifier
//                .padding(28.dp, 40.dp, 28.dp, 0.dp)
//                .fillMaxWidth(1f),
//            fontSize = 30.sp,
//            textAlign = TextAlign.Center,
//            color = Color.Black
//        )
//    }
//
//    // ...
//    val weatherApiViewModel = viewModel<WeatherApiViewModel>()
//    val weatherLiveData = weatherApiViewModel.weatherData
//    weatherApiViewModel.getWeatherData("London", "2022-08-20")
//
//    // Create a StateFlow to hold the weather data
//    val weatherState = remember { MutableStateFlow(Pair(0.0, 0.0)) }
//
//    // Collect the data from the LiveData and update the StateFlow
//    LaunchedEffect(weatherLiveData) {
//        weatherLiveData.observeForever {
//            weatherState.value = it ?: Pair(0.0, 0.0)
//        }
//    }
//
//    // Read the weather data from the StateFlow
//    val weatherData = weatherState.collectAsState().value
//    Log.e("DisplayWeatherContent", weatherData.toString())
//}

    // ...
//    var weatherPair = WeatherApiCall(destination = destination, date = startDate)
//    var avgTemp = weatherPair?.first
//    var totalPrecip = weatherPair?.second

//    Log.d("DisplayWeatherContent", avgTemp.toString())
//    Log.d("DisplayWeatherContent", totalPrecip.toString())
//
//    if (totalPrecip != null) {
//        if (avgTemp != null) {
//            Log.e("DisplayWeatherContent", "Null checks passed")
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Average Temperature: $avgTemp °F",
//                    fontSize = 20.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color.Black,
//                    modifier = Modifier.padding(16.dp)
//                )
//                Text(
//                    text = "Total Precipitation: $totalPrecip in",
//                    fontSize = 20.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color.Black,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }

// }

//                Text(text = "Average Temperature: $avgTemp F",
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color.Black)
//                Text(text = "Total Precipitation: $totalPrecip in",
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color.Black
//                )



//@Composable
//fun WeatherApiCall(destination: String?, date: String?): Pair<Double, Double>? {
//    val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=London&dt=2022-08-20"
//    val request = Request.Builder().url(url).build()
//    val client = OkHttpClient()
//    var avgTemp by remember { mutableStateOf(0.0) }
//    var totalPrecip by remember { mutableStateOf(0.0) }
//
//    LaunchedEffect(key1 = destination, key2 = date) {
//        val response = withContext(Dispatchers.IO) {
//            client.newCall(request).execute()
//        }
//
//        if (response.isSuccessful) {
//            val jsonString = response.body?.string() //Get the JSON object as a string
//            val jsonObject = JSONObject(jsonString) //Get the string as a jsonArray for easier parsing
//            avgTemp = jsonObject
//                .getJSONObject("forecast")
//                .getJSONArray("forecastday")
//                .getJSONObject(0)
//                .getJSONObject("day")
//                .getDouble("avgtemp_f")
//            totalPrecip = jsonObject
//                .getJSONObject("forecast")
//                .getJSONArray("forecastday")
//                .getJSONObject(0)
//                .getJSONObject("day")
//                .getDouble("totalprecip_in")
//            Log.d("avgTemp", avgTemp.toString())
//            Log.d("precip", totalPrecip.toString())
//        } else {
//            Log.e("WeatherApiCall", "API Response Failed")
//        }
//    }
//
//    return Pair(avgTemp, totalPrecip)
//}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeatherView(context: Context, navController: NavHostController, customModifier: Modifier, tripID: String?, destination: String?, startDate: String?, endDate: String?) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
        DisplayWeatherContent(context, tripID, destination, startDate, endDate)
        FloatingActionButton(
            backgroundColor = Color(0xFFFFC107),
            contentColor = Color(0xFF00FF00),
            modifier = Modifier.padding(310.dp, 520.dp, 8.dp, 8.dp),

            onClick = {
                navController.navigate("CompletedTripView/$tripID")
            }
        ) {
            Icon(Icons.Default.ArrowBack, "back")
        }
    })
}

//@Composable
//fun WeatherApiCall(destination: String?, date: String?): Pair<Double, Double>?{
//    //val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=${destination}&dt=${date}"
//    val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=London&dt=2022-08-20"
////    Log.e("WeatherApiCall", url)
//    val request = Request.Builder().url(url).build()
//    val client = OkHttpClient()
//    var avgTemp by remember { mutableStateOf(0.0) }
//    var totalPrecip by remember { mutableStateOf(0.0) }
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onResponse(call: Call, response: Response) {
//            Log.d("WeatherApiCall", "API Response Succeeded")
//            val jsonString = response.body?.string() //Get the JSON object as a string
//            val jsonObject = JSONObject(jsonString) //Get the string as a jsonArray for easier parsing
//            Log.d("JSON OBJECT", jsonObject.toString())
//            avgTemp = jsonObject
//                .getJSONObject("forecast")
//                .getJSONArray("forecastday")
//                .getJSONObject(0)
//                .getJSONObject("day")
//                .getDouble("avgtemp_f")
//            totalPrecip = jsonObject
//                .getJSONObject("forecast")
//                .getJSONArray("forecastday")
//                .getJSONObject(0)
//                .getJSONObject("day")
//                .getDouble("totalprecip_in")
//            Log.d("avgTemp", avgTemp.toString())
//            Log.d("precip", totalPrecip.toString())
//            // TO DO: display avgTemp and totalPrecip in Composable functions using Text()
//
//        }
//
//        override fun onFailure(call: Call, e: java.io.IOException) {
//            Log.e("WeatherApiCall", "API Response Failed")
//        }
//    })
//    return Pair(avgTemp, totalPrecip)
//
//}

@Preview
@Composable
fun ShowWeatherStats() {
    Log.e("DisplayWeatherPair", "DISPLAYING WEATHER PAIR")
    Text(
        text = "Hello",
        modifier = Modifier
            .padding(28.dp, 40.dp, 28.dp, 0.dp)
            .fillMaxWidth(1f),
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
}



//@Composable
//fun WeatherApiCall(destination: String?, date: String?){
//    val coroutineScope = rememberCoroutineScope()
//    val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=${destination}&dt=${date}"
////    Log.e("WeatherApiCall", url)
//    val request = Request.Builder().url(url).build()
//    val client = OkHttpClient()
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onResponse(call: Call, response: Response) {
//            Log.d("WeatherApiCall", "API Response Succeeded")
//            if (response.isSuccessful) {
//                Log.d("WeatherApiCall", "response.isSuccessful passes")
//                val jsonString = response.body?.string() //Get the JSON object as a string
//                val jsonObject = JSONObject(jsonString) //Get the string as a jsonArray for easier parsing
//                Log.d("JSON OBJECT", jsonObject.toString())
//                val avgTemp = jsonObject
//                    .getJSONObject("forecast")
//                    .getJSONArray("forecastday")
//                    .getJSONObject(0)
//                    .getJSONObject("day")
//                    .getDouble("avgtemp_f")
//                val totalPrecip = jsonObject
//                    .getJSONObject("forecast")
//                    .getJSONArray("forecastday")
//                    .getJSONObject(0)
//                    .getJSONObject("day")
//                    .getDouble("totalprecip_in")
//                Log.d("avgTemp", avgTemp.toString())
//                Log.d("precip", totalPrecip.toString())
//                DisplayAvgTemp(temp = avgTemp.toString())
//                DisplayPrecip(precip = totalPrecip.toString())
//
//            }
//        }
//
//        override fun onFailure(call: Call, e: java.io.IOException) {
//            Log.e("WeatherApiCall", "API Response Failed")
//        }
//    })
//
//}



//@Composable
////fun WeatherApiCall(destination: String?, date: String?): WeatherDataArray{ //Makes a call to our Weather API Using OkHttp, returns JSON object with avg temp and precip
//fun WeatherApiCall(destination: String?, date: String?){
//    val weatherArray = ObservableArrayList<WeatherData>()
//
//    Log.e("GetWeather", "Attempting to Fetch JSON")
//
//    val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=${destination}&dt=${date}"
//
//    val request = Request.Builder().url(url).build()
//
//    val client = OkHttpClient()
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onResponse(call: Call, response: Response) {
//            Log.e("WeatherApiCall", "API Response Succeeded")
//            if (response.isSuccessful) {
//                Log.e("WeatherApiCall", "RESPONSE SUCCESSFUL")
//                val weatherReceived = try { JSONObject(response.body?.string() ?: "").getJSONArray("weatherArray") } catch (e: JSONException) { JSONArray() }
//            }
//            // val body = response.body?.string()
//            // Log.e("JSON Body:", body.toString())
//
//        }
//
//        override fun onFailure(call: Call, e: java.io.IOException) {
//            Log.e("WeatherApiCall", "API Response Failed")
//        }
//    })
//
//}

class WeatherClass(var avg_temp: String? = null,
                   var precip: String? = null){
}


//class Chatt(var username: String? = null,
//            var message: String? = null,
//            var timestamp: String? = null,
//            audio: String? = null) {
//    var audio: String? by ChattPropDelegate(audio)
//}
//class WeatherDataArray(val weatherData: List<edu.umich.aehill.reminiscetest.WeatherData>)
//
//    class WeatherData(
//        val location: Location,
//        val forecast: Forecast
//    )
//
//    class Location(
//        val name: String,
//        val region: String,
//        val country: String,
//        val lat: Double,
//        val lon: Double,
//        val tz_id: String,
//        val localtime_epoch: Long,
//        val localtime: String
//    )
//
//    class Forecast(
//        val forecastday: List<ForecastDay>
//    )
//
//    class ForecastDay(
//        val date: String,
//        val date_epoch: Long,
//        val day: DayData,
////    val astro: AstroData,
//        val hour: List<HourData>
//    )
//
//    class DayData(
//        val avgtemp_f: Double, // EXTRACT THIS
//        val totalprecip_in: Double, // EXTRACT THIS
////    val condition: ConditionData
//    )

//data class AstroData(
//    // Define properties as needed
//)

    class HourData(
        val time_epoch: Long,
        val time: String,
        val temp_f: Double,
//    val condition: ConditionData,
        val precip_in: Double
    )








//@Composable
//fun WeatherApiCall(destination: String?, date: String?){
//    Log.e("WeatherApiCall", "Fetching JSON")
//    val url = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=London&dt=2023-01-01"
//    // val url = "https://api.letsbuildthatapp.com/youtube/home_feed"
//    Log.e("WeatherApiCall", url)
//    val request = Request.Builder().url(url).build()
//
//    val client = OkHttpClient()
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onResponse(call: Call, response: Response) {
//            Log.e("WeatherApiCall", "API Response Succeeded")
//
//            if (response != null) {
//                val body = response.body?.string()
//                Log.e("JSON Body:", body.toString())
//
//                // TO DO: Extract DayData from the JSON response, display it in our UI
//                val gson = GsonBuilder().create()
//                val weatherApiResponse = gson.fromJson(body, WeatherDataArray::class.java)
//                Log.e("Weather API Response:", weatherApiResponse.toString())
//            }
//
//        }
//        override fun onFailure(call: Call, e: java.io.IOException) {
//            Log.e("WeatherApiCall", "API Response Failed")
//        }
//    })
//}

//@Composable
////fun WeatherApiCall(destination: String?, date: String?): WeatherDataArray{ //Makes a call to our Weather API Using OkHttp, returns JSON object with avg temp and precip
//fun WeatherApiCall(destination: String?, date: String?){
//
//    Log.e("GetWeather", "Attempting to Fetch JSON")
//
//    val baseUrl = "https://api.weatherapi.com".toHttpUrlOrNull() // Replace with your base URL
//    val pathSegments = listOf("v1", "history.json") // Replace with your path segments
//    val queryParameters = mapOf("key" to "4b255d0791e04160af3200558232803","q" to destination, "dt" to date) // Replace with your query parameters
//    val urlBuilder = baseUrl?.newBuilder()
//    for (segment in pathSegments) {
//        urlBuilder?.addPathSegment(segment)
//    }
//    for ((name, value) in queryParameters) {
//        urlBuilder?.addQueryParameter(name, value)
//    }
//    val url = urlBuilder?.build()
//    Log.e("URL",url.toString()) // Should be http://api.weatherapi.com
//
//    val apiUrl = "https://api.weatherapi.com/v1/history.json?key=4b255d0791e04160af3200558232803&q=London&dt=2023-01-01"
//    val request = url?.let {
//        Request.Builder() //Builds our request
//            .url(it.toString())
//            .build()
//    }
//
//    val request = Request.Builder().url(apiUrl).build()
//
//    val client = OkHttpClient()
//
//    client.newCall(request).enqueue(object : Callback { //Adds API request to queue
//
//        override fun onResponse(call: Call?, response: Response?) {
//            if (response != null) {
//                Log.e("getWeather", "PASSED GET request")
//                Log.e("getWeather", "Failed GET request")
//                val body = response.body?.string()
//                Log.e("JSON Body:", body.toString())
//
//                // TO DO: Extract DayData from the JSON response, display it in our UI
//                val gson = GsonBuilder().create()
//                val weatherApiResponse = gson.fromJson(body, WeatherDataArray::class.java)
//                Log.e("Weather API Response:", weatherApiResponse.toString())
//
//            }
//        }
//
//        override fun onFailure(call: Call, e: IOException) {
//            Log.e("getWeather", "FAILED GET request")
//            Log.e("Request:", request.toString())
//        }
//
//    })
//}

//fun getWeather(context: Context) {
//    val getRequest = JsonObjectRequest(url,
//        { response ->
//            _chatts.clear()
//            val weatherReceived = try { response.getJSONArray("weatherData") } catch (e: JSONException) { JSONArray() }
//            for (i in 0 until weatherReceived.length()) {
//                val weatherEntry = chattsReceived[i] as JSONArray
//                if (weatherEntry.length() == nFields) {
//                    _weather.add(WeatherClass(username = weatherEntry[0].toString(),
//                        message = weatherEntry[1].toString(),
//                        timestamp = weatherEntry[2].toString(),
//                        audio = weatherEntry[3].toString()
//                    ))
//                } else {
//                    Log.e("getWeather", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + nFields.toString())
//                }
//            }
//        }, { }
//    )
//
//    if (!this::queue.isInitialized) {
//        queue = newRequestQueue(context)
//    }
//    queue.add(getRequest)
//}
//}



// ORIGINAL FROM LAB 3:
//fun getChatts(context: Context) {
//    val getRequest = JsonObjectRequest(serverUrl+"getaudio/",
//        { response ->
//            _chatts.clear()
//            val chattsReceived = try { response.getJSONArray("chatts") } catch (e: JSONException) { JSONArray() }
//            for (i in 0 until chattsReceived.length()) {
//                val chattEntry = chattsReceived[i] as JSONArray
//                if (chattEntry.length() == nFields) {
//                    _chatts.add(Chatt(username = chattEntry[0].toString(),
//                        message = chattEntry[1].toString(),
//                        timestamp = chattEntry[2].toString(),
//                        audio = chattEntry[3].toString()
//                    ))
//                } else {
//                    Log.e("getChatts", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + nFields.toString())
//                }
//            }
//        }, { }
//    )
//
//    if (!this::queue.isInitialized) {
//        queue = newRequestQueue(context)
//    }
//    queue.add(getRequest)
//}
//}
