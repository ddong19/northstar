package edu.umich.aehill.reminiscetest

import android.os.Bundle
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


class MainActivity : ComponentActivity() {
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
        ))


        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "MainView") {
                composable("MainView") {
                    MainView(this@MainActivity, navController)
                }
                composable("TestView"){
                    TestView(this@MainActivity, navController)
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