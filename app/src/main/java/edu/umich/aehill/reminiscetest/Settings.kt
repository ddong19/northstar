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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Settings(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = {
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
                                .align(Alignment.CenterHorizontally) // Center the text horizontally
                        )

                        Text(
                            text = "This is the SETTINGS page",
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally) // Center the text horizontally
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { navController.navigate("LoginView") },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp)
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = "Logout",
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
