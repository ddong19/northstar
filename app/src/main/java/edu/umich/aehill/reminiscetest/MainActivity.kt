package edu.umich.aehill.reminiscetest

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import android.Manifest
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import android.view.View



class MainActivity : ComponentActivity() {
    //    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        val controller = window.insetsController
//        controller?.apply {
//            hide(WindowInsets.Type.statusBars())
//            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {

        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
                if (!it.value) {
                    Log.e("Mainview", "permission denied")
                    finish()
                }
            }
        }.launch(arrayOf(Manifest.permission.CAMERA,
            //Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.ACCESS_FINE_LOCATION
        ))

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        setContent {
            val navController = rememberNavController()

            // Declaring 2 Colors
            val colorStart = Color(255, 131, 95)
            val colorEnd = Color(87, 67, 174)

            // Creating a Horizontal Gradient Color
            val mainGradient = Brush.verticalGradient(0f to colorStart, 500f to colorEnd)

            val reusableModifier = Modifier
                .background(mainGradient)
                .fillMaxSize()
            val loginViewModifier = Modifier
                .fillMaxSize()

            NavHost(navController, startDestination = "LoginView") {
                composable("MainView") {
                    MainView(this@MainActivity, navController, reusableModifier)
                }
                composable("Settings"){
                    Settings(this@MainActivity, navController, reusableModifier)
                }
                composable("TripDetailsView"){
                    TripDetailView(this@MainActivity, navController, reusableModifier)
                }
                composable("TripPageView/{destination}",
                    arguments = listOf(navArgument("destination") { type = NavType.StringType })
                ){ navBackStackEntry ->
                    /* Extracting the destination from the route */
                    TripPageView(this@MainActivity, navController, reusableModifier, navBackStackEntry.arguments?.getString("destination"))
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
                composable("LoginView"){
                    LoginView(this@MainActivity, navController, loginViewModifier)
                }
                composable("TripStatisticsView"){
                    TripStatisticsView(this@MainActivity, navController, reusableModifier)
                }
                composable("SpotifyView"){
                    SpotifyView(this@MainActivity, navController, reusableModifier)
                }
                composable("WeatherView/{destination}/{startDate}/{endDate}",
                    arguments = listOf(navArgument("destination") { type = NavType.StringType }, navArgument("startDate") { type = NavType.StringType }, navArgument("endDate") { type = NavType.StringType })
                )
                { navBackStackEntry ->
                    WeatherView(this@MainActivity, navController, reusableModifier, "3", navBackStackEntry.arguments?.getString("destination"),navBackStackEntry.arguments?.getString("startDate"),navBackStackEntry.arguments?.getString("endDate"))
                }
                composable("SlideshowEditView"){
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