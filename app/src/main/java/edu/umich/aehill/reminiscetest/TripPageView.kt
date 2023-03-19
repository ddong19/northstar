package edu.umich.aehill.reminiscetest


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack


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
    var uploadedImages by rememberSaveable {mutableStateOf(false)}
    var finishedTrip by rememberSaveable {mutableStateOf(false)}
    val customMod = Modifier
        .fillMaxWidth(
            fraction = if (uploadedImages) 0f else 1f
        )
        .background(
            color = if (uploadedImages) Color.Transparent else MaterialTheme.colors.primary
        )



    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents())
        { uris: List<Uri>? ->
            if (uris != null) {
                imageUris = uris
            }
        }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=customMod) {
        Button(
            onClick = {
                launcher.launch("image/*")
                uploadedImages = true
            }



        ) {
            Text(
                text = if(uploadedImages) "" else "Select Images",
                color = if(uploadedImages) MaterialTheme.colors.onBackground else Color.White
            )
        }
        Button(
            onClick = {
                navController.navigate("CompletedTripView")
            }
        ) {
            Text(
                text = if(finishedTrip) "" else "Finish Trip",
                color = if(finishedTrip) MaterialTheme.colors.onBackground else Color.White
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp), // 3 images per row
            contentPadding = PaddingValues(16.dp),
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


