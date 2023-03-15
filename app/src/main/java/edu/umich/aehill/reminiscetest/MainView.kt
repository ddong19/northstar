package edu.umich.aehill.reminiscetest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.ReminisceTestTheme
import android.util.Log
import androidx.compose.ui.graphics.Shape


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(context: Context, navController: NavHostController) {
    ReminisceTestTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Reminisce test app",
                        fontSize = 20.sp
                    )
                })
            },
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = Color(0xFFFFC107),
                    contentColor = Color(0xFF00FF00),
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
                    onClick = {
                        // navigate to PostView
                        navController.navigate("TestView")
                    }
                ) {
                    Icon(Icons.Default.ArrowForward, "fwd")
                }
            }
        ) {
            Greeting("You are in the main view!")
        }
    }

}