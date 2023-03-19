package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.content.pm.PackageManager
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TravelMapView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
        Greeting("you are in the travel map page view")

        FloatingActionButton(
            backgroundColor = Color(0xFFFFC107),
            contentColor = Color(0xFF00FF00),
            modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),

            onClick = {
                navController.navigate("MainView")
            }
        ) {
            Icon(Icons.Default.ArrowForward, "fwd")
        }

    })
}