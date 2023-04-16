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
import android.util.Log
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import edu.umich.aehill.reminiscetest.TripStore.postNewTrip

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripDetailsViewContent(context: Context, navController: NavHostController){

    var spotifyUsername by remember { mutableStateOf("") }
    var tripDescription by remember { mutableStateOf("")}
    var tripLocation by remember { mutableStateOf("")}
    var tripStartDate by remember { mutableStateOf("") }
    var tripEndDate by remember { mutableStateOf("") }
    var tripFriend1 by remember { mutableStateOf("") }
    var tripFriend2 by remember { mutableStateOf("") }
    var menu1Expanded by remember { mutableStateOf(false) }
    var menu2Expanded by remember { mutableStateOf(false) }
    val friendMenuOptions = getUserMenuOptions(tripFriend1, tripFriend2)


    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        Text(
            text = "Enter your trip details!",
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = Color.White
            )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripLocation,
            onValueChange = {
                tripLocation = it
                Log.e("tripdetailsview", "value entered into trip location field")},
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("City", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripStartDate,
            onValueChange = {
                if (it.matches("^\\d{0,8}\$".toRegex())) {
                    tripStartDate = it
                }
            },
            visualTransformation = DateTransformation(),
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Start Date (MM/DD/YYYY)", textAlign=TextAlign.Start, fontSize = 18.sp)
            }

        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripEndDate,
            onValueChange = {
                if (it.matches("^\\d{0,8}\$".toRegex())) {
                    tripEndDate = it
                }
            },
            visualTransformation = DateTransformation(),
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("End Date (MM/DD/YYYY)", textAlign=TextAlign.Start, fontSize = 18.sp)
            }

        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = spotifyUsername,
            onValueChange = {
                spotifyUsername = it
                Log.e("tripdetailsview", "value entered into spotify field")},
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Playlist ID", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        OutlinedTextField(
            value = tripDescription,
            onValueChange = {
                tripDescription = it
                Log.e("tripdetailsview", "value entered into description")
                            },
            modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            label = {
                Text("Description", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        /*
        src:
        https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
         */

        ExposedDropdownMenuBox(
            expanded = menu1Expanded,
            onExpandedChange = {
                menu1Expanded = !menu1Expanded
            }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = tripFriend1,
                onValueChange = { },
                label = {Text("Friend Username 1", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menu1Expanded
                    )
                },
                modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            ExposedDropdownMenu(
                expanded = menu1Expanded,
                onDismissRequest = {
                    menu1Expanded = false
                }
            ) {
                friendMenuOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            tripFriend1 = selectionOption
                            menu1Expanded = false
                        }
                    ){
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth(1f)) {
        /*
        src:
        https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
         */

        ExposedDropdownMenuBox(
            expanded = menu2Expanded,
            onExpandedChange = {
                menu2Expanded = !menu2Expanded
            }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = tripFriend2,
                onValueChange = { },
                label = {Text("Friend Username 2", Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp), textAlign=TextAlign.Start, fontSize = 18.sp) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menu1Expanded
                    )
                },
                modifier = Modifier.padding(8.dp, 20.dp, 8.dp, 0.dp).fillMaxWidth(1f),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            ExposedDropdownMenu(
                expanded = menu2Expanded,
                onDismissRequest = {
                    menu2Expanded = false
                }
            ) {
                friendMenuOptions.forEach { selectionOption ->
                    if(selectionOption != tripFriend1){

                        DropdownMenuItem(
                            onClick = {
                                tripFriend2 = selectionOption
                                menu2Expanded = false
                            }
                        ){
                            Text(text = selectionOption)
                        }
                    }
                }
            }
        }
    }

    // This would be where the right arrow button will be.
    Row(
        horizontalArrangement = Arrangement.End, // Align to the far right side of the screen
        modifier = Modifier.fillMaxWidth().padding(15.dp) // Add padding to make sure it doesn't touch the edge of the screen
    ) {
        IconButton(onClick = {

            // add people to array
            var tripFriendsString = tripFriend1

            if(tripFriend2 != "" && tripFriend2 != tripFriend1){ // account for empty & duplicates
                tripFriendsString += "," + tripFriend2
            }
            
            postNewTrip(context, tripStartDate, tripEndDate, tripLocation, spotifyUsername, tripDescription, tripFriendsString)
            navController.navigate("TripPageView/$tripLocation")
        }) {
            Image(
                painter = painterResource(R.drawable.forwardbutton),
                contentDescription = "Right Arrow",
                colorFilter = ColorFilter.tint(Color.White), // Make the arrow gray
                modifier = Modifier.size(48.dp)

            )
        }
    }
}

fun getUserMenuOptions(friendOne : String = "", friendTwo : String = "") : MutableList<String> {

    var options = mutableListOf<String>()

    UserStore.users.allUsers?.forEach { user ->
        var newUsernameString = user.username
        if(newUsernameString != UserStore.users.currentUser?.username &&
                newUsernameString != friendOne &&
                newUsernameString != friendTwo){
            options = (options + newUsernameString) as MutableList
        }
    }

    return options
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripDetailView(context: Context, navController: NavHostController, customModifier: Modifier) {
    ScaffoldBack(context = context, navController = navController, customModifier = customModifier,
        content = { TripDetailsViewContent(context = context, navController = navController)

        }
    )
}



/* inspired by https://stackoverflow.com/questions/68468942/how-to-apply-a-mask-date-mm-dd-yyyy-in-textfield-with-jetpack-compose
* */
fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset +1
            if (offset <= 8) return offset +2
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <=2) return offset
            if (offset <=5) return offset -1
            if (offset <=10) return offset -2
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}

class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}



