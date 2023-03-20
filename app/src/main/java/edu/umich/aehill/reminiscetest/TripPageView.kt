package edu.umich.aehill.reminiscetest


import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.ExifInterface
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
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.logging.Logger.global


@Composable
fun TripPageContent(context: Context, navController: NavHostController, destination: String?){
    // getting latest tripID
    /*
    SELECT `id`
    FROM `table`
    ORDER BY `date added` DESC
    LIMIT 1
 */
    val user_id = 3
    Log.e("TripPageView", "querying for curr trip id is happening")

    var serverUrl = "https://34.75.243.151/getalltrips/$user_id"
    var nFields = 7 // number of fields that each trip should have returned
    var returnTripId = "2" // TODO: change?

    Log.e("TripPageView", "User id is $user_id")
    val queue = Volley.newRequestQueue(context)

    val getRequest = JsonObjectRequest(serverUrl,
        { response ->
            val tripsReceived = try { response.getJSONArray("trips") } catch (e: JSONException) { JSONArray() }
            Log.e("TripPageView", "trips received length is $tripsReceived.length()")
            // get the first trip in the array
            if(tripsReceived.length() > 0){
                val tripEntry = tripsReceived[0] as JSONArray
                if(tripEntry.length() == nFields){
                    returnTripId = tripEntry[0].toString()  // TODO: is this the trip id? should be i think
                    Log.e("TripPageView", "most recent completed trip query $returnTripId")
                }
                else{
                    Log.e("TripPageView", "error with calling the most recently completed trip query")
                }
            }
        }, {  }

    )

    queue.add(getRequest)

    Log.e("TripPageView", "return trip id is $returnTripId")



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
                Log.e("TripPageView", "button clicked")
//                var tripId = queryForMostRecentTripID(context,3).toInt() // TODO: change to actual user rn is dan2
                var tripId = Global.currentTripID
                Log.e("query for most recent trip id", "query just happened and it returned $tripId")
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
//                        "trip_id" to queryForMostRecentTripID(context,3).toInt(), // TODO: change to actual user
                        "trip_id" to Global.currentTripID,
                        "image_location" to location.toString(),
                        "image_uri" to imageUris[index].toString(),
                    )
                    var serverUrl = "https://34.75.243.151" // not sure ab this
                    val postRequest = JsonObjectRequest(
                        Request.Method.POST,
                        serverUrl+"/postimage/", JSONObject(jsonObj),
                        {
                            Log.d("postImage", "image data posted to $returnTripId!")
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
    queryForMostRecentTripID(context, 3)
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripPageContent(context = context, navController = navController, destination = destination ) })
}


