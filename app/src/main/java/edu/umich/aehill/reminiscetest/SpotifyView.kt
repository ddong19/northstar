package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import edu.umich.aehill.reminiscetest.SpotifyStore.currentSpotify
import edu.umich.aehill.reminiscetest.UserStore.users


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SpotifyView(context: Context, navController: NavHostController, customModifier: Modifier) {


    BackHandler {
        navController.popBackStack()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        val backgroundBrush = Brush.verticalGradient(
            colors = listOf(Color(0xFF3B3B3B), Color(0xFF181818)),
            startY = 0f,
            endY = Float.POSITIVE_INFINITY
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(horizontal = 16.dp)
        ) {
            TopAppBar(
                backgroundColor = Color.Transparent,
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
                        text = "${users.currentUser?.username}",
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

            Spacer(modifier = Modifier.height(24.dp))

            var text = "Spotify!"
            if(currentSpotify.playlistName != null){
                text = currentSpotify.playlistName!!
            }

            Text(
                text = text,
                fontSize = 30.sp,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(50.dp))

            LazyColumn( modifier = Modifier.fillMaxSize(),
            ){
                currentSpotify.songs?.forEach { song ->
                    item (
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = song.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = { /* Play song */ },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "▶",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

            }


            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    try {
                        val uri = Uri.parse("https://open.spotify.com/album/4eLPsYPBmXABThSJ821sqY")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                    }
                },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
            ) {
                Text(
                    text = "Open Spotify Playlist",
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
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
                    Icon(
                        painter = airplaneIcon,
                        contentDescription = "Current Trip",
                        modifier = Modifier.size(22.dp), // Set the desired size for your icon
                        tint = Color.White
                    )
                },
                label = { Text("Current Trip") }, // Change the label to "Airplane"
                selected = false,
                onClick = {
                   // Log.d("ScaffoldBack/Navigate to CompletedTripView", "tripid is ${currentTrip.tripId}")
                    navController.navigate("CompletedTripView/$currentTrip.tripId")

                }
            )
        }
    }
}

//// DAMN: https://open.spotify.com/album/4eLPsYPBmXABThSJ821sqY
//IconButton(onClick = {
//    try{
//        val uri = Uri.parse("https://open.spotify.com/album/4eLPsYPBmXABThSJ821sqY")
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        context.startActivity(intent)
//    }
//    catch(e : ActivityNotFoundException){
//    }
//}) {
//    Image(
//        painter = painterResource(id = R.drawable.spotify),
//        contentDescription = "Spotify",
//        modifier = Modifier.size(48.dp),
//        contentScale = ContentScale.Fit
//    )
//}
//Text("Spotify", color = Color.White)