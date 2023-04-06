package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompletedTripContent(context: Context, navController: NavHostController) {

    var isLaunching by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isLaunching) {
            isLaunching = false
            TripStore.updateCurrentTrip(context, 3) // user id is 3
        }
    }

    // UI for trip name and thumbnail
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(1f)) {
        FloatingActionButton(
            backgroundColor = Color(0xFF808080),
            contentColor = Color(0xFF000000),
            modifier = Modifier.padding(35.dp, 25.dp, 0.dp, 30.dp),

            onClick = {
                //implement in MVP
            }
        ) {
            Icon(Icons.Default.AddCircle, "add")
        }
        Log.d("destination", "${currentTrip.destination}")
        Text(
            //need to get tripLocation from input from the user
            text = currentTrip.destination.toString(),
            modifier = Modifier
                .padding(8.dp, 5.dp, 25.dp, 0.dp)
                .fillMaxWidth(1f),
            fontSize = 55.sp,
            textAlign = TextAlign.Right
        )
    }


    var showSlideshow by remember { mutableStateOf(false) }

    val images = listOf(
        "https://cdn.pixabay.com/photo/2023/03/11/07/36/bird-7843879_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/13/18/09/red-tulips-7850506_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/11/57/flowers-7852176_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/12/41/ornamental-cherry-7852285_1280.jpg",
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {



        Box(modifier = Modifier.fillMaxHeight()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 64.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.weather),
                        contentDescription = "Weather",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text("Weather", color = Color.White)
                }
                IconButton(onClick = {
                    navController.navigate("SpotifyView")
                }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.spotify),
                            contentDescription = "Spotify",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text("Spotify", color = Color.White)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        val intent = Intent(context, MapsActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = "Map",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text("Map", color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        showSlideshow = !showSlideshow
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.slideshow),
                            contentDescription = "Slideshow",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text("Slideshow", color = Color.White)
                }
            }


            Row() {
                if (showSlideshow) {
                    if (currentTrip.imageURIs?.size!! > 0) {  // i have no idea what this line means
                        Card(
                            modifier = Modifier.padding(10.dp, 150.dp, 8.dp, 10.dp),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            AutoSlidingCarousel(
                                itemsCount = currentTrip.imageURIs!!.size,
                                itemContent = { index ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(currentTrip.imageURIs!![index].URI)
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.height(250.dp)
                                    )
                                    Log.d("completed trip view", currentTrip.imageURIs!![index].URI)
                                }
                            )
                        }
                    } else {
                        Card(
                            modifier = Modifier.padding(10.dp, 150.dp, 8.dp, 10.dp),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            AutoSlidingCarousel(
                                itemsCount = images.size,
                                itemContent = { index ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(images[index])
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.height(250.dp)
                                    )
                                }
                            )
                        }
                    }


                } else {
                    // photo grid appears
                    val imageUris = currentTrip.imageURIs
                    val bitmaps = remember { mutableStateListOf<Bitmap>() }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(128.dp), // 3 images per row
                            contentPadding = PaddingValues(5.dp, 0.dp, 5.dp, 300.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
//                    Log.d("get Uri", "Uri IS: $imageUris")
//                    Log.d("get size Uri", "Uri size is: $imageUris.size" )
                            if (imageUris != null) {
                                items(imageUris.size) { index ->
                                    val bitmap = bitmaps.getOrNull(index) ?: run {
                                        val newBitmap = if (Build.VERSION.SDK_INT < 28) {
                                            MediaStore.Images.Media.getBitmap(
                                                context.contentResolver,
                                                imageUris.get(index).URI.toUri()
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
                    }
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompletedTripView(context: Context, navController: NavHostController, customModifier: Modifier, tripId: String?) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = { CompletedTripContent(context, navController) })
}



























