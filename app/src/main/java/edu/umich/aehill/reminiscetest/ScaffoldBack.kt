package edu.umich.aehill.reminiscetest.ui.theme

import edu.umich.aehill.reminiscetest.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import edu.umich.aehill.reminiscetest.UserStore.users

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldBack(context: Context, navController: NavHostController, customModifier: Modifier, content: @Composable() () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color(255, 131, 95),
                elevation = 0.dp,
                title = {
                    Text(
                        text = "Reminisce",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                actions = {
                    Text(
                        text = "${users.currentUser?.username}", // Replace with the desired text
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, // Add this property to make the text bold
                        color = Color.White
                    )

                    IconButton(
                        onClick = {
                            // Handle profile icon click
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                elevation = 0.dp,
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
                    onClick = {
                        Log.d("ScaffoldBack/Navigate to CompletedTripView", "tripid is ${currentTrip.tripId}")
                        navController.navigate("CompletedTripView/$currentTrip.tripId")

                    }
                )
            }
        },
    ) {
        Column(modifier = customModifier) {
            content()
        }
    }
}

