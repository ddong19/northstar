package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlideshowView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, navigateTo = "CompletedTripView", content = { Greeting("you are in the shlidshow page view") })
}