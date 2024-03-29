package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import edu.umich.aehill.reminiscetest.TripStore.currentTrip
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import edu.umich.aehill.reminiscetest.UserStore.users


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompletedTripContent(context: Context, navController: NavHostController) {

    var isLaunching by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isLaunching) {
            isLaunching = false
            TripStore.updateCurrentTrip(context, users.currentUser?.userId!!.toInt())
        }
    }

    // UI for trip name and thumbnail
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
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(1f)) {
        Spacer(modifier = Modifier.height(24.dp))
        Log.d("THUMBNAIL URI COMPLETED: ", "${currentTrip.thumbnailURI}")
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentTrip.thumbnailUri)
                .crossfade(enable = true)
                .build(),
            contentDescription = "Avatar Image",
            modifier = Modifier.size(78.dp),
            contentScale = ContentScale.Crop,
        )

        Log.d("destination", "${currentTrip.destination}")
        Text(
            text = currentTrip.destination.toString(),
            modifier = Modifier
                .padding(8.dp, 5.dp, 25.dp, 0.dp)
                .fillMaxWidth(1f),
            fontSize = 55.sp,
            textAlign = TextAlign.Right
        )
    }


    var showSlideshow by remember { mutableStateOf(false) }
    var showWeather by remember { mutableStateOf(false) }

    // THIS WAS FOR ERROR TESTING - IT IS NOT HARDCODED AND IS NEVER USED
    val images = listOf(
        "https://cdn.pixabay.com/photo/2023/03/11/07/36/bird-7843879_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/13/18/09/red-tulips-7850506_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/11/57/flowers-7852176_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/12/41/ornamental-cherry-7852285_1280.jpg",
    )

    val tripImagesAndFriendImages = mutableListOf<TripImage>()

    currentTrip.imageURIs?.let { tripImagesAndFriendImages.addAll(it) }
    currentTrip.friendOneImageURIs?.let { tripImagesAndFriendImages.addAll(it) }
    currentTrip.friendTwoImageURIs?.let { tripImagesAndFriendImages.addAll(it) }


    tripImagesAndFriendImages.shuffle() // interleave

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                //TO DO: Add function call to new composable function that defines weather button
//                WeatherButton(
//                    context = context,
//                    navController = navController,
//                    customModifier = Modifier,
//                    tripId = "3",
//                    destination = "${currentTrip.destination}"
//                )
                IconButton(onClick = {
                    showWeather = !showWeather
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.weather),
                        contentDescription = "Weather",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Text("Weather", color = Color.White)
            }
            if (showWeather){
                WeatherView(
                    context = context,
                    navController = navController,
                    customModifier = Modifier,
                    destination = "${currentTrip.destination}",
                    startDate = "${currentTrip.startDate}",
                    endDate = "${currentTrip.endDate}"
                )
                Log.e("Weather", "SHOWING WEATHER")
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
            IconButton(onClick = {
                navController.navigate("TripStatisticsView")
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.statistics),
                        contentDescription = "Statistics",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text("Stats", color = Color.White)
                }
            }
        }

        Row( modifier = Modifier.weight(1f, false)) {
            if (showSlideshow) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        modifier = Modifier.padding(0.dp,10.dp,0.dp,0.dp),
                        onClick = {
                            navController.navigate("SlideshowEditView")
                        }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Edit Slideshow", color = Color.White)
                        }
                    }
                    if (tripImagesAndFriendImages.size > 0) {
                        Card(
                            modifier = Modifier.padding(10.dp, 110.dp, 8.dp, 10.dp),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            AutoSlidingCarousel(
                                itemsCount = tripImagesAndFriendImages.size,
                                itemContent = { index ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(tripImagesAndFriendImages[index].URI)
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.height(250.dp)
                                    )
                                }
                            )
                        }
                    } else {
                        // This is for error testing and should never run, that is why it has hard
                        // coded images in it
                        Log.d("running", "running in wrong one")
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
                }


            } else {
                // photo grid appears
                val imageUris = currentTrip.imageURIs
                val bitmaps = remember { mutableStateListOf<Bitmap>() }

                Column(
                    //modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(128.dp), // 3 images per row
                        contentPadding = PaddingValues(5.dp, 0.dp, 5.dp, 300.dp),
                        //modifier = Modifier.fillMaxWidth()
                    ) {
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



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompletedTripView(context: Context, navController: NavHostController, customModifier: Modifier, tripId: String?) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = { CompletedTripContent(context, navController) })
}




























