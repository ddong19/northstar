package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import kotlin.collections.*



@Composable
@SuppressLint("UnrememberedMutableState")
fun deleteImageRow(bitmap: Bitmap, image: TripImage) {
    var buttonText by remember { mutableStateOf("Delete Photo") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Image",
            modifier = Modifier
                .size(180.dp)
                .fillMaxWidth()
                .aspectRatio(1f) // maintain aspect ratio of image
                .padding(4.dp), // add padding between images,
            contentScale = ContentScale.Fit
        )
        Button(
            onClick = {
                Log.d("deleting photo", "photo deleted!")
                Log.d("imageURI size", "${currentTrip.imageURIs?.size}")
                currentTrip.imageURIs?.remove(image)
                Log.d("imageURI size", "${currentTrip.imageURIs?.size}")
                buttonText = "Deleted!"
            }
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
@SuppressLint("UnrememberedMutableState")
fun SlideshowEditContent(context: Context) {
    val imageUris = currentTrip.imageURIs
    var index = 0
    val numPhotos = currentTrip.imageURIs?.size
    val bitmaps = remember { mutableStateListOf<Bitmap>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUris != null) {
            while (index < numPhotos!!) {
                val bitmap = bitmaps.getOrNull(index) ?: run {
                    val newBitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            context.contentResolver,
                            imageUris[index].URI.toUri()
                        )
                    } else {
                        val source = ImageDecoder.createSource(
                            context.contentResolver,
                            imageUris[index].URI.toUri()
                        )
                        ImageDecoder.decodeBitmap(source)
                    }
                    bitmaps.add(newBitmap)
                    newBitmap
                }
                deleteImageRow(bitmap = bitmap, imageUris[index])
                index += 1
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SlideshowEditView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(
        context = context,
        navController = navController,
        customModifier = customModifier,
        content = {
            SlideshowEditContent(context = context)
            FloatingActionButton(
                backgroundColor = Color(0xFFFFC107),
                contentColor = Color(0xFF00FF00),
                modifier = Modifier.padding(310.dp, 520.dp, 8.dp, 8.dp),
                onClick = {
                    navController.navigate("CompletedTripView/{${currentTrip.tripId}}")
                }
            ) {
                Icon(Icons.Default.ArrowForward, "fwd")
            }
        })
}