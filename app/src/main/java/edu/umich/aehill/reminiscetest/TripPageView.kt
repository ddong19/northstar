package edu.umich.aehill.reminiscetest


import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import org.json.JSONObject


@Composable
fun TripPageContent(context: Context, navController: NavHostController){
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
            text = "Cancun",
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
                navController.navigate("CompletedTripView")
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
                    val id = ContentUris.parseId(imageUris[index])
                    val location = imageUris[index].toString()
                    val jsonObj = mapOf(
                        "trip_id" to "dandong", // TODO: get trip id
                        "image_id" to id,
                        "image_location" to location,
                        "image_uri" to imageUris[index],
                    )
                    var serverUrl = "34.75.243.151" // not sure ab this
                    val postRequest = JsonObjectRequest(
                        Request.Method.POST,
                        serverUrl+"postImages/", JSONObject(jsonObj),
                        {
                            Log.d("postImage", "image data posted!")
                        },
                        { error -> Log.e("postImage", error.localizedMessage ?: "JsonObjectRequest error") }
                    )
                    imageInfo.add(Pair(id, location))
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
fun TripPageView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripPageContent(context = context, navController = navController) })
}


