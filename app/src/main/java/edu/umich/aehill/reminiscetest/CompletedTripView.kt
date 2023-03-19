package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import org.json.JSONArray
import org.json.JSONException

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompletedTripView(context: Context, navController: NavHostController, customModifier: Modifier, tripId: String?) {
//    var lat = MainActivity().lat
//    var long = MainActivity().long
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("This is the completed trip view", fontSize = 24.sp, modifier = Modifier.padding(16.dp), color = Color.White)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
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
                            navController.navigate("SlideshowView")
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
    })
}

fun getAllImagesForTrip(context: Context, tripId: String): ArrayList<String> {

    var serverUrl = "https://34.75.243.151/gettripimages/$tripId"

    val queue = Volley.newRequestQueue(context)

    var imageURIs = arrayListOf<String>()

    val getRequest = JsonObjectRequest(serverUrl,
        { response ->
            val imagesReceived = try { response.getJSONArray("images") } catch (e: JSONException) { JSONArray() }
            for (i in 0 until imagesReceived.length()) {
                val image = imagesReceived[i] as JSONArray
                if (image.length() == 3) {
                    imageURIs = (imageURIs + image[2].toString()) as ArrayList<String>
                } else {
                    Log.e("getAllImagesForTrip", "Received unexpected number of fields: " + image.length().toString() + " instead of 3")
                }
            }

        }, {  }

    )

    queue.add(getRequest)

    return imageURIs

}
