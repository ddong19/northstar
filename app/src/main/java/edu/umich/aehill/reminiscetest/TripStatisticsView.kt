package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import edu.umich.aehill.reminiscetest.TripStore.currentTrip

@Composable
fun TripStatisticsContent(context: Context) {
    var userMostPhotos by remember { mutableStateOf("") }
    var userLeastPhotos by remember { mutableStateOf("") }
    var userAllDistances by remember { mutableStateOf(Pair("", "")) }

    userMostPhotos = userMostPhotos()
    userLeastPhotos = userLeastPhotos()
    userAllDistances = userDistances()
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
            modifier = Modifier.padding(28.dp, 60.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "$userMostPhotos",
            modifier = Modifier.padding(28.dp, 25.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }

    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "User who took the least amount of photos on the trip",
            modifier = Modifier.padding(28.dp, 65.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "$userLeastPhotos",
            modifier = Modifier.padding(28.dp, 25.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "User that travelled the longest distance",
            modifier = Modifier.padding(28.dp, 70.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "${userAllDistances.first}",
            modifier = Modifier.padding(28.dp, 20.dp, 28.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

fun userMostPhotos(): String {
    var userFriends = currentTrip.friends?.split(",")
    Log.d("userFriends","userFriends are: $userFriends")
    var userMostPhotos = ""
    if (currentTrip.imageURIs?.size!! > currentTrip.friendOneImageURIs?.size!!) {
        if (currentTrip.imageURIs?.size!! > currentTrip.friendTwoImageURIs?.size!!) {
            // user has most photos
            userMostPhotos = "dan2"
        } else {
            // friend2 has most photos
            if (userFriends!!.size > 1) {
                userMostPhotos = userFriends[1]
            }
        }
    } else if (currentTrip.friendOneImageURIs?.size!! > currentTrip.friendTwoImageURIs?.size!!) {
        // friend1 has most photos
        if (userFriends!!.isNotEmpty()) {
            userMostPhotos = userFriends[0]
        }
    } else {
        if (userFriends!!.size > 1) {
            userMostPhotos = userFriends[1]
        }
    }

    Log.d("userMostPhotos", "user with most photos is $userMostPhotos")
    return userMostPhotos
}
fun userLeastPhotos(): String {
    var userFriends = currentTrip.friends?.split(",")
    Log.d("userFriends","userFriends are: $userFriends")
    var userLeastPhotos = ""

    if (currentTrip.imageURIs?.size!! < currentTrip.friendOneImageURIs?.size!!) {
        if (currentTrip.imageURIs?.size!! < currentTrip.friendTwoImageURIs?.size!!) {
            // user has most photos
            userLeastPhotos = "dan2"
        } else {
            // friend2 has most photos
            if (userFriends!!.size > 1) {
                userLeastPhotos = userFriends[1]
            }
        }
    } else if (currentTrip.friendOneImageURIs?.size!! < currentTrip.friendTwoImageURIs?.size!!) {
        // friend1 has most photos
        if (userFriends!!.isNotEmpty()) {
            userLeastPhotos = userFriends[0]
        }
    } else {
        if (userFriends!!.size > 1) {
            userLeastPhotos = userFriends[1]
        }
    }

    Log.d("userMostPhotos", "user with most photos is $userLeastPhotos")
    return userLeastPhotos
}

fun userDistances(): Pair<String, String> {
    var userFriends = currentTrip.friends?.split(",")
    Log.d("userFriends","userFriends are: $userFriends")
    val userPoints = listOf(
        LatLng(42.2808, 83.7430),
        LatLng(43.6532, 79.3832),
        LatLng(51.1215, 114.0076),
        LatLng(51.0899, 115.3441),
        LatLng(51.1784, 115.5708)
    )
    var maxDist = 0.0
    var maxUser = ""
    var minDist = 0.0
    var minUser = ""
    var allPoints = listOf(
        userPoints,
        //currentTrip.friendOneImageLocation
        //currentTrip.friendTwoImageLocation
    )
    for (i in 0 until allPoints.size - 1){
        if (allPoints[i] == null){
            continue
        }
        var dist = calculateDistance(allPoints[i])
        if (dist > maxDist){
            maxDist = dist
            maxUser?.let {
                maxUser = if (i == 0) {
                    "dan2"
                } else ({
                    maxUser = userFriends?.get(i-1) ?: ""
                }).toString()
            }
        }
        if (dist < minDist){
            minDist = dist
            minUser?.let {
                minUser = if (i == 0) {
                    "dan2"
                } else ({
                    minUser = userFriends?.get(i-1) ?: ""
                }).toString()
            }
        }
    }
    return Pair(maxUser, minUser)
}

fun calculateDistance(pts: List<LatLng>): Double {
    var totalDistance = 0.0
    for (i in 0 until pts.size - 1){
        val startPoint = Location("locationA")
        startPoint.latitude = pts[i].latitude
        startPoint.longitude = pts[i].longitude

        val endPoint = Location("locationB")
        endPoint.latitude = pts[i+1].latitude
        endPoint.longitude = pts[i+1].longitude

        totalDistance += startPoint.distanceTo(endPoint)
    }
    return totalDistance
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TripStatisticsView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
        TripStatisticsContent(context)
        FloatingActionButton(
            backgroundColor = Color(0xFFFFC107),
            contentColor = Color(0xFF00FF00),
            modifier = Modifier.padding(310.dp, 0.dp, 8.dp, 8.dp),


            onClick = {
                navController.navigate("CompletedTripView/${currentTrip.tripId}")
            }
        ) {
            Icon(Icons.Default.ArrowForward, "fwd")
        }
    })
}