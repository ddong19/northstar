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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TestView(context: Context, navController: NavHostController, customModifier: Modifier) {
    Column(modifier = customModifier){
        Greeting("You are in the test view!")
        FloatingActionButton(
            backgroundColor = Color(0xFFFFC107),
            contentColor = Color(0xFF00FF00),
            modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
            onClick = {
                // navigate to PostView
                navController.navigate("MainView")
            }
        ) {
            Icon(Icons.Default.ArrowForward, "fwd")
        }
    }
}