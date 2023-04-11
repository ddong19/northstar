


package edu.umich.aehill.reminiscetest


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import edu.umich.aehill.reminiscetest.TripStore.updateCurrentTrip
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import org.json.JSONObject

// adapted photo picker source code from https://gist.github.com/stevdza-san/053b170dbb3176b17c04915abf59a3e4
@Composable
fun TripPageContent(context: Context, navController: NavHostController, destination: String?){

    var isLaunching by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isLaunching) {
            isLaunching = false
            updateCurrentTrip(context, 3) // user id is 3
        }
    }


    val user_id = 3
    val queue = Volley.newRequestQueue(context)
    var thumbnailUri: Any? by remember { mutableStateOf(R.drawable.baseline_add_circle_outline_24) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            Log.d("PhotoPicker", "Selected URI: $it")
            thumbnailUri = it
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        Spacer(modifier = Modifier.height(24.dp))
        AsyncImage(
            modifier = Modifier
                .padding(start = 50.dp)
                .size(100.dp)
                .clickable {
                    photoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailUri)
                .crossfade(enable = true)
                .build(),
            contentDescription = "Avatar Image",
            contentScale = ContentScale.Crop,
        )
        Text(
            //need to get tripLocation from input from the user

            text = if (destination == null) "Cancun" else "$destination",
            modifier = Modifier
                .padding(8.dp, 75.dp, 25.dp, 0.dp)
                .fillMaxWidth(1f),
            fontSize = 55.sp,
            textAlign = TextAlign.Right
        )
        val jsonObj = mapOf(
            "trip_id" to currentTrip.tripId,
            "thumbnail_uri" to thumbnailUri,
        )
        var serverUrl = "https://34.75.243.151"
        val postRequestThumb = JsonObjectRequest(
            Request.Method.POST,
            serverUrl+"/posthumbnail/", JSONObject(jsonObj),
            {
                Log.d("posThumbnail", "thumbnail data posted to ${currentTrip.tripId}!")
            },
            { error -> Log.e("postImage", error.localizedMessage ?: "JsonObjectRequest error") }
        )
        queue.add(postRequestThumb)

    }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current
    val bitmaps = remember { mutableStateListOf<Bitmap>() }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents())
        { uris: List<Uri>? ->
            if (uris != null) {
                imageUris = uris
            }
        }
    Log.d("get Uri", "Uri IS: $imageUris")
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
                navController.navigate("CompletedTripView/$currentTrip.tripId")
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
            Log.d("get Uri", "Uri IS: $imageUris")
            Log.d("get size Uri", "Uri size is: $imageUris.size" )
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
                    val location = getLatLong(context, imageUris[index])
                    val full_uri = imageUris[index].toString().split("/")
                    Log.d("full uri", "$full_uri")
                    val id = full_uri[full_uri.size - 1]
                    Log.d("get id", "ID IS: $id")
                    Log.d("get location", "location IS: $location")
                    val jsonObj = mapOf(
                        "trip_id" to currentTrip.tripId,
                        "image_location" to location.toString(),
                        "image_uri" to imageUris[index].toString(),
                    )
                    var serverUrl = "https://34.75.243.151"
                    val postRequest = JsonObjectRequest(
                        Request.Method.POST,
                        serverUrl+"/postimage/", JSONObject(jsonObj),
                        {
                            Log.d("postImage", "image data posted to ${currentTrip.tripId}!")
                        },
                        { error -> Log.e("postImage", error.localizedMessage ?: "JsonObjectRequest error") }
                    )
                    queue.add(postRequest)
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

@Composable
fun getLatLong(context: Context, uri: Uri): Pair<Double, Double>? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val filePath = cursor.getString(columnIndex)
        val exifInterface = ExifInterface(filePath)
        val latLong = FloatArray(2)
        if (exifInterface.getLatLong(latLong)) {
            return Pair(latLong[0].toDouble(), latLong[1].toDouble())
        }
    }
    return Pair(-50.0, -50.0)
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripPageView(context: Context, navController: NavHostController, customModifier: Modifier, destination: String?) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripPageContent(context = context, navController = navController, destination = destination ) })
}