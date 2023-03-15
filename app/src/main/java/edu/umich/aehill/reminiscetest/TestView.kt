package edu.umich.aehill.reminiscetest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ReminisceTestTheme
import android.util.Log
import androidx.compose.material.icons.filled.ArrowForward
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TestView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, navigateTo = "MainView", content = { Greeting("you are in the test view") })
}