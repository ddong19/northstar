package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack

@Composable
fun TripStatisticsContent(context: Context, tripID: String?) {

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "Here are your Trip Statistics!",
            modifier = Modifier.padding(28.dp, 40.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "User who took the most photos on the trip",
            modifier = Modifier.padding(28.dp, 80.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "User who took the least amount of photos on the trip",
            modifier = Modifier.padding(28.dp, 100.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "User that travelled the longest distance",
            modifier = Modifier.padding(28.dp, 120.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}






@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TripStatisticsView(context: Context, navController: NavHostController, customModifier: Modifier, tripID: String?) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
        TripStatisticsContent(context, "3")
        FloatingActionButton(
            backgroundColor = Color(0xFFFFC107),
            contentColor = Color(0xFF00FF00),
            modifier = Modifier.padding(310.dp, 520.dp, 8.dp, 8.dp),


            onClick = {
                navController.navigate("CompletedTripView/$tripID")
            }
        ) {
            Icon(Icons.Default.ArrowForward, "fwd")
        }
    })
}