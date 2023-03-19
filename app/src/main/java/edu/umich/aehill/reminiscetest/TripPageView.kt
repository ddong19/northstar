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
import androidx.compose.foundation.layout.*
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

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents())
        { uris: List<Uri>? ->
            if (uris != null) {
                imageUris = uris
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUris.forEachIndexed { index, uri ->
            if (Build.VERSION.SDK_INT < 28) {
                bitmaps.getOrNull(index) ?: run {
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    bitmaps.add(bitmap)
                }
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                bitmaps.add(bitmap)
            }

            bitmaps.getOrNull(index)?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(20.dp)
                )
            }
        }

        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Select Images")
        }
    }


    Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick Image")
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = {
                navController.navigate("CompletedTripView")
            }
        ) {
            Text(text = "Finish Trip")
        }
    }


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripPageView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripPageContent(context = context, navController = navController) })
}


