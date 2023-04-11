package edu.umich.aehill.reminiscetest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainView(context: Context, navController: NavHostController, customModifier: Modifier) {

    var isLaunching by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isLaunching) {
            isLaunching = false
            TripStore.updateCurrentTrip(context, 3) // user id is 3
        }
    }

    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = {
            val backgroundBrush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Transparent),
                start = Offset.Zero,
                end = Offset(Float.POSITIVE_INFINITY, 0f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "Welcome!",
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(brush = backgroundBrush)
                                .height(48.dp)
                                .padding(horizontal = 10.dp)
                                .align(Alignment.CenterHorizontally) // Center the text horizontally
                        )

                    }
                }

               Spacer(modifier = Modifier.height(150.dp))


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = {navController.navigate("TripDetailsView")},
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp)
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = "Create A New Trip!",
                            fontSize = 24.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(72.dp))
            }

        }
    )
}
