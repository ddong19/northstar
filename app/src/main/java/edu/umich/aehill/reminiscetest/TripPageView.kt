package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import android.os.ext.SdkExtensions.getExtensionVersion
import android.provider.MediaStore.ACTION_PICK_IMAGES
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


@Composable
fun TripPageContent(context: Context){
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        FloatingActionButton(
            backgroundColor = Color(0xFF808080),
            contentColor = Color(0xFF000000),
            modifier = Modifier.padding(35.dp, 90.dp, 0.dp, 0.dp),

            onClick = {
                //handlePhotoPickerLaunch()
            }
        ) {
            Icon(Icons.Default.AddCircle, "add")
        }
        Text(
            //need to get tripLocation from input from the user
            text = "Cancun",
            modifier = Modifier.padding(8.dp, 75.dp, 25.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 55.sp,
            textAlign = TextAlign.Right
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        FloatingActionButton(
            backgroundColor = Color(0xFF808080),
            //contentColor = Color(0xFF000000),
            modifier = Modifier.padding(35.dp, 90.dp, 0.dp, 0.dp),

            onClick = {
                //handlePhotoPickerLaunch()
            }
        ) {
            Icon(Icons.Default.AddCircle, "add")
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripPageView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripPageContent(context = context)
            FloatingActionButton(
                backgroundColor = Color(0xFFFFC107),
                contentColor = Color(0xFF00FF00),
                modifier = Modifier.padding(300.dp, 300.dp, 8.dp, 8.dp),

                onClick = {
                    navController.navigate("CompletedTripView")
                }
            ) {
                Icon(Icons.Default.ArrowForward, "fwd")
            }
        })
}

/*
private fun isPhotoPickerAvailable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getExtensionVersion(Build.VERSION_CODES.R) >= 2
    } else {
        false
    }
}

fun handlePhotoPickerLaunch() {
    if (isPhotoPickerAvailable()) {
        // Registers a photo picker activity launcher in multi-select mode.
        // In this example, the app allows the user to select up to 5 media files.
        val pickMultipleMedia =
            registerForActivityResult(PickMultipleVisualMedia(5)) { uris ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        // Launch the photo picker and allow the user to choose only images.
        pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    } else {
        // Consider implementing fallback functionality so that users can still
        // select images and videos.
    }
}

*/
