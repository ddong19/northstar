package edu.umich.aehill.reminiscetest.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldBack(context: Context, navController: NavHostController, customModifier: Modifier, navigateTo: String, content: @Composable() () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Reminisce",
                    fontSize = 20.sp
                )
            })
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("home") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.AccountCircle, "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { navController.navigate("profile") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { navController.navigate("settings") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Color(0xFFFFC107),
                contentColor = Color(0xFF00FF00),
                modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),

                onClick = {
                    navController.navigate(navigateTo)
                }
            ) {
                Icon(Icons.Default.ArrowForward, "fwd")
            }
        }
    ) {
        Column(modifier = customModifier) {
            content()
        }
    }
}

