package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import org.json.JSONArray
import org.json.JSONException
import edu.umich.aehill.reminiscetest.AutoSlidingCarousel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompletedTripContent(context: Context, tripId: String?) {

    // UI for trip name and thumbnail
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        FloatingActionButton(
            backgroundColor = Color(0xFF808080),
            contentColor = Color(0xFF000000),
            modifier = Modifier.padding(35.dp, 90.dp, 0.dp, 0.dp),

            onClick = {
                //implement in MVP
            }
        ) {
            Icon(Icons.Default.AddCircle, "add")
        }
        Log.d("destination", "${Global.currentTripLocation}")
        Text(
            //need to get tripLocation from input from the user
            text = Global.currentTripLocation,
            modifier = Modifier
                .padding(8.dp, 75.dp, 25.dp, 0.dp)
                .fillMaxWidth(1f),
            fontSize = 55.sp,
            textAlign = TextAlign.Right
        )
    }



    var showSlideshow by remember { mutableStateOf(false) }

    val images = listOf(
        "content://com.android.providers.media.documents/document/image%3A1000000023",
        "https://cdn.pixabay.com/photo/2023/03/11/07/36/bird-7843879_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/13/18/09/red-tulips-7850506_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/11/57/flowers-7852176_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/12/41/ornamental-cherry-7852285_1280.jpg",
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Your Trip Summary", fontSize = 24.sp, modifier = Modifier.padding(16.dp), color = Color.White)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(){
                if(showSlideshow) {
                    Card(
                        modifier = Modifier.padding(10.dp, 150.dp, 8.dp, 10.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        AutoSlidingCarousel(
                            itemsCount = images.size,
                            itemContent = { index ->
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(images[index])
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.height(250.dp)
                                )
                            }
                        )
                    }

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 64.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.weather),
                        contentDescription = "Weather",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text("Weather", color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.spotify),
                        contentDescription = "Spotify",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text("Spotify", color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        val intent = Intent(context, MapsActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = "Map",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text("Map", color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        showSlideshow = !showSlideshow
                        Log.e("CompletedTripView", "showSlideshow is now $showSlideshow")
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.slideshow),
                            contentDescription = "Slideshow",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text("Slideshow", color = Color.White)
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompletedTripView(context: Context, navController: NavHostController, customModifier: Modifier, tripId: String?) {
//    var lat = MainActivity().lat
//    var long = MainActivity().long

    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = { CompletedTripContent(context, tripId) })
}
