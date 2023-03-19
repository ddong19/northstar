package edu.umich.aehill.reminiscetest


import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils.split
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


@Composable
fun TripPageContent(context: Context, navController: NavHostController, destination: String?){
    val queue = Volley.newRequestQueue(context)

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        FloatingActionButton(
            backgroundColor = Color(0xFF808080),
            contentColor = Color(0xFF000000),
            modifier = Modifier.padding(35.dp, 90.dp, 0.dp, 0.dp),

            onClick = {
                //
            }
        ) {
            Icon(Icons.Default.AddCircle, "add")
        }
        Text(
            //need to get tripLocation from input from the user

            text = if (destination == null) "Cancun" else "$destination",
            modifier = Modifier
                .padding(8.dp, 75.dp, 25.dp, 0.dp)
                .fillMaxWidth(1f),
            fontSize = 55.sp,
            textAlign = TextAlign.Right
        )
    }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current
    val bitmaps = remember { mutableStateListOf<Bitmap>() }
    val imageInfo = mutableListOf<Pair<Long, String>>()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents())
        { uris: List<Uri>? ->
            if (uris != null) {
                imageUris = uris
            }
        }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Select Images")
        }
        Button(
            onClick = {
                Log.e("TripPageView", "button clicked")
                var tripId = queryForMostRecentTripID(context,3).toInt() // TODO: change to actual user rn is dan2

                Log.e("TripPageView", "trip id is $tripId")
                navController.navigate("CompletedTripView/$tripId")
            }
        ) {
            Text(text = "Finish Trip")
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp), // 3 images per row
            contentPadding = PaddingValues(5.dp, 0.dp, 5.dp, 300.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(imageUris.size) { index ->
                val bitmap = bitmaps.getOrNull(index) ?: run {
                    val newBitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, imageUris[index])
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, imageUris[index])
                        ImageDecoder.decodeBitmap(source)
                    }
                    bitmaps.add(newBitmap)
                    Log.d("image uris", "$imageUris")
                    val full_uri = imageUris[index].toString().split("A")
                    Log.d("full uri", "$full_uri")
                    val id = full_uri[1]
                    Log.d("get id", "ID IS: $id")
                    val location = imageUris[index].toString()
                    Log.d("get location", "location IS: $location")
                    val jsonObj = mapOf(
                        "trip_id" to queryForMostRecentTripID(context,3).toInt(), // TODO: change to actual user
                        "image_location" to location.toString(),
                        "image_uri" to imageUris[index].toString(),
                    )
                    var serverUrl = "https://34.75.243.151" // not sure ab this
                    val postRequest = JsonObjectRequest(
                        Request.Method.POST,
                        serverUrl+"/postimage/", JSONObject(jsonObj),
                        {
                            Log.d("postImage", "image data posted!")
                        },
                        { error -> Log.e("postImage", error.localizedMessage ?: "JsonObjectRequest error") }
                    )
                    queue.add(postRequest)
                    imageInfo.add(Pair(id.toLong(), location))
                    newBitmap
                }

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // maintain aspect ratio of image
                        .padding(4.dp) // add padding between images
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    }


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripPageView(context: Context, navController: NavHostController, customModifier: Modifier, destination: String?) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripPageContent(context = context, navController = navController, destination = destination ) })
}


