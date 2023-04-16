package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import edu.umich.aehill.reminiscetest.ui.theme.ScaffoldBack
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer
/* source code built on top of https://blog.protein.tech/jetpack-compose-auto-image-slider-with-dots-indicator-45dfeba37712 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideshowViewContent(context: Context, tripId: String) {

    // replace with logic for obtaining list of users photos from database

    val images = listOf(
        "https://cdn.pixabay.com/photo/2023/03/11/07/36/bird-7843879_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/13/18/09/red-tulips-7850506_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/11/57/flowers-7852176_1280.jpg",
        "https://cdn.pixabay.com/photo/2023/03/14/12/41/ornamental-cherry-7852285_1280.jpg",
    )

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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    // autoSlideDuration: Long = ,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val timer = remember {
        fixedRateTimer(
            initialDelay = 1000.toLong(),
            period = 2900.toLong()
        ) {
            coroutineScope.launch {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { timer.cancel() }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(pageCount = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want
        // the transparent background
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = pagerState.targetPage,
                dotSize = 8.dp
            )
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SlideshowView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier, content = {
        //SlideshowViewContent(context = context)
        FloatingActionButton(
            backgroundColor = Color(0xFFFFC107),
            contentColor = Color(0xFF00FF00),
            modifier = Modifier.padding(310.dp, 30.dp, 8.dp, 8.dp),


            onClick = {
                navController.navigate("MainView")
            }
        ) {
            Icon(Icons.Default.ArrowForward, "fwd")
        }
    })
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Yellow,
    unSelectedColor: Color = Color.Gray,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}
