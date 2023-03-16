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
import android.util.Log
import androidx.annotation.RequiresApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripPageView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, navigateTo = "CompletedTripView",
        content = {
        Greeting("you are in the trip page view") })
}
private fun isPhotoPickerAvailable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getExtensionVersion(Build.VERSION_CODES.R) >= 2
    } else {
        false
    }
}
public static final String ACTION_PICK_IMAGES
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
