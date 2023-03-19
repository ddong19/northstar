package edu.umich.aehill.reminiscetest

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.umich.aehill.reminiscetest.ui.theme.FancyLoginScreenTheme
import android.net.Uri
import android.media.MediaPlayer
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.VideoView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.foundation.layout.Box
import android.widget.MediaController
import androidx.compose.ui.draw.alpha
import android.util.Log
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import android.graphics.Color as AndroidColor




@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginPage(context: Context, navController: NavHostController, customModifier: Modifier) {
    val passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    val videoUri = getVideoUri(context)

    FancyLoginScreenTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    videoUri = videoUri,
                    modifier = Modifier.fillMaxSize()
                )
                LoginColumn(context, navController, customModifier)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Reminisce",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 60.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}




@Composable
fun VideoPlayer(videoUri: Uri, modifier: Modifier) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setBackgroundColor(AndroidColor.TRANSPARENT)
                setVideoURI(videoUri)
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    Log.d("VideoPlayer", "Video prepared and starting")
                    start()
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("VideoPlayer", "Error occurred: what=$what, extra=$extra")
                    true
                }
            }
        },
        modifier = modifier
    ) { view ->
        view.setMediaController(null) // This line hides the media controller, add this import: import android.widget.MediaController
    }
}






@Composable
fun LoginColumn(context: Context, navController: NavHostController, customModifier: Modifier) {
    val passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = customModifier
            .fillMaxSize()
            .padding(24.dp)
            .alpha(0.65f),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add your app logo here, if you don't have a logo, comment or remove this section
        /*
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.White
        )
        */
        TextInput(InputType.Name, keyboardActions = KeyboardActions(onNext = {
            passwordFocusRequester.requestFocus()
        }))
        TextInput(InputType.Password, keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            context.doLogin()
        }), focusRequester = passwordFocusRequester)
        Button(onClick = {
            //context.doLogin()
            navController.navigate("MainView")
        }, modifier = Modifier.fillMaxWidth()) {
            Text("SIGN IN", Modifier.padding(vertical = 8.dp))
        }
        Divider(
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 48.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", color = Color.White)
            TextButton(onClick = {}) {
                Text("SIGN UP", color = Color.White)
            }
        }
    }
}


private fun Context.doLogin() {
    Toast.makeText(
        this,
        "Something went wrong, try again later!",
        Toast.LENGTH_SHORT
    ).show()
}

private fun getVideoUri(context: Context): Uri {
    val rawId = context.resources.getIdentifier("clouds", "raw", context.packageName)
    val videoUri = "android.resource://${context.packageName}/$rawId"
    return Uri.parse(videoUri)
}

sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Name : InputType(
        label = "Username",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "Password",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun TextInput(
    inputType: InputType,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions
) {
    var value by remember { mutableStateOf("") }

    TextField(
        value = value,
        onValueChange = { value = it },
        modifier = Modifier
            .fillMaxWidth()
            .focusOrder(focusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, contentDescription = null) },
        label = { Text(text = inputType.label) },
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions
    )
}

