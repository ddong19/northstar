package edu.umich.aehill.reminiscetest.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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

