package edu.umich.aehill.reminiscetest.ui.theme

import edu.umich.aehill.reminiscetest.R
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldBack(context: Context, navController: NavHostController, customModifier: Modifier, content: @Composable() () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                elevation = 0.dp,
                title = {
                    Text(
                        text = "Reminisce",
                        fontSize = 20.sp,
                    )
                },
                actions = { // Add this block to include actions on the right side of the top app bar
                    IconButton(
                        onClick = {
                            // Handle profile icon click
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        }, // Set the Scaffold background color to transparent
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.White // Apply the white color tint
                        )
                    },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("MainView") }
                )
                BottomNavigationItem(
                    icon = {
                        val airplaneIcon = painterResource(id = R.drawable.airplane_icon)
                        Image(
                            painter = airplaneIcon,
                            contentDescription = "Current Trip",
                            modifier = Modifier.size(22.dp), // Set the desired size for your icon
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    },
                    label = { Text("Current Trip") }, // Change the label to "Airplane"
                    selected = false,
                    onClick = { navController.navigate("CompletedTripView") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { navController.navigate("settings") }
                )
            }
        },
    ) {
        Column(modifier = customModifier) {
            content()
        }
    }
}

