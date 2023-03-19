package edu.umich.aehill.reminiscetest

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import edu.umich.aehill.reminiscetest.ui.theme.ReminisceTestTheme
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.properties.Delegates


class MainActivity : ComponentActivity() {
//    private var lat = 40.0
//    private var long = 40.0
//    public fun getLat(): Double {
//        return lat
//    }
//    public fun getLong(): Double {
//        return long
//    }
    override fun onCreate(savedInstanceState: Bundle?) {

//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
//            results.forEach {
//                if (!it.value) {
//                    Log.e("Mainview", "permission denied")
//                    finish()
//                }
//            }
//        }.launch(arrayOf(Manifest.permission.CAMERA,
//            //Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_VIDEO,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ))
//
//        // GETTING LOCATION
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        LocationServices.getFusedLocationProviderClient(applicationContext)
//            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    lat = it.result.latitude
//                    Log.e("Latitude", lat.toString())
//                    long = it.result.longitude
//                    Log.e("Longitude", long.toString())
//                } else {
//                    Log.e("PostActivity getFusedLocation", it.exception.toString())
//                }
//            }
//        // END GETTING LOCATION
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Declaring 2 Colors
            val colorStart = Color(255, 131, 95)
            val colorEnd = Color(87, 67, 174)

            // Creating a Horizontal Gradient Color
            val mainGradient = Brush.verticalGradient(0f to colorStart, 1000f to colorEnd)

            val reusableModifier = Modifier
                .background(mainGradient)
                .fillMaxSize()

            NavHost(navController, startDestination = "MainView") {
                composable("MainView") {
                    MainView(this@MainActivity, navController, reusableModifier)
                }
                composable("Settings"){
                    Settings(this@MainActivity, navController, reusableModifier)
                }
                composable("TripDetailsView"){
                    TripDetailView(this@MainActivity, navController, reusableModifier)
               }
               composable("TripPageView"){
                    TripPageView(this@MainActivity, navController, reusableModifier)
                }
                composable("CompletedTripView/{tripId}",
                    arguments = listOf(navArgument("tripId") { type = NavType.StringType })
                ){ navBackStackEntry ->
                    /* Extracting the id from the route */
                    CompletedTripView(this@MainActivity, navController, reusableModifier, navBackStackEntry.arguments?.getString("tripId"))
                }
                composable("TravelMapView"){
                    TravelMapView(this@MainActivity, navController, reusableModifier)
                }
                composable("SlideshowView"){
                    SlideshowView(this@MainActivity, navController, reusableModifier)
                }
            }
        }
    }
}

@Composable
fun Greeting(str: String) {
    Text(text = "This is my greeting: $str!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReminisceTestTheme {
        Greeting("Default preview")
    }
}