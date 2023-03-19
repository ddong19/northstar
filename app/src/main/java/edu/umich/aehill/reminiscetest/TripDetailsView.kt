package edu.umich.aehill.reminiscetest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import com.android.volley.toolbox.Volley


// add to onclick for navigation button
fun postTripDetails(context: Context, startDate: String, endDate: String, destination: String, spotifyUsername: String,
description: String) {

    val queue = Volley.newRequestQueue(context)

    val jsonObj = mapOf(
        "user_id" to 3, // TODO: change this to the actual user
        "trip_destination" to destination,
        "trip_start" to startDate,
        "trip_end" to endDate,
        "trip_spotify" to spotifyUsername,
        "trip_description" to description
        //TODO: add other columns here
    )

    Log.e("TripDetailsView", "Posting trip now!")

    var serverUrl = "https://34.75.243.151" // not sure ab this
    val postRequest = JsonObjectRequest(Request.Method.POST,
        serverUrl+"/posttrip/", JSONObject(jsonObj),
        {
            Log.d("postTrip", "trip posted!")
        },
        { error -> Log.e("postTrip", error.localizedMessage ?: "JsonObjectRequest error") }
    )

    queue.add(postRequest)
}

@Composable
fun TripDetailsViewContent(context: Context, navController: NavHostController){

    var spotifyUsername by remember { mutableStateOf("") }
    var tripDescription by remember { mutableStateOf("")}
    var tripLocation by remember { mutableStateOf("")}
    var tripStartDate by remember { mutableStateOf("") }
    var tripEndDate by remember { mutableStateOf("") }

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "Enter your trip details!",
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 25.sp,
            textAlign = TextAlign.Center
            )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripLocation,
            onValueChange = {
                tripLocation = it
                Log.e("tripdetailsview", "value entered into trip location field")},
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Destination", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripStartDate,
            onValueChange = {
                if (it.matches("^\\d{0,8}\$".toRegex())) {
                    tripStartDate = it
                }
            },
            visualTransformation = DateTransformation(),
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Start Date (MM/DD/YYYY)", textAlign=TextAlign.Start, fontSize = 18.sp)
            }

        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripEndDate,
            onValueChange = {
                if (it.matches("^\\d{0,8}\$".toRegex())) {
                    tripEndDate = it
                }
            },
            visualTransformation = DateTransformation(),
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("End Date (MM/DD/YYYY)", textAlign=TextAlign.Start, fontSize = 18.sp)
            }

        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = spotifyUsername,
            onValueChange = {
                spotifyUsername = it
                Log.e("tripdetailsview", "value entered into spotify field")},
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Spotify", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripDescription,
            onValueChange = {
                tripDescription = it
                Log.e("tripdetailsview", "value entered into description")
                            },
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Description", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp)
            }
        )
    }
    // This would be where the right arrow button will be.
    Row(
        horizontalArrangement = Arrangement.End, // Align to the far right side of the screen
        modifier = Modifier.fillMaxWidth().padding(8.dp) // Add padding to make sure it doesn't touch the edge of the screen
    ) {
        IconButton(onClick = {
            postTripDetails(context, tripStartDate, tripEndDate, tripLocation, spotifyUsername, tripDescription)
            navController.navigate("TripPageView")
        }) {
            Image(
                painter = painterResource(R.drawable.forwardbutton),
                contentDescription = "Right Arrow",
                colorFilter = ColorFilter.tint(Color.White), // Make the arrow gray
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripDetailView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripDetailsViewContent(context = context, navController = navController)

        }
    )
}


/* inspired by https://stackoverflow.com/questions/68468942/how-to-apply-a-mask-date-mm-dd-yyyy-in-textfield-with-jetpack-compose
* */
fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset +1
            if (offset <= 8) return offset +2
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <=2) return offset
            if (offset <=5) return offset -1
            if (offset <=10) return offset -2
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}

class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}



