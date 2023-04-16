package edu.umich.aehill.reminiscetest

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object UserStore {
    var users = Users()
    private lateinit var queue: RequestQueue
    val serverUrl = "https://34.75.243.151"

    fun getUserIdFromUsername(users: MutableList<SingleUser>?, username: String): String? {
        // loop over & create users arr
        if (users != null) {
            for (item in users) {
                if (item.username == username) {
                    return item.userId
                }
            }
        }

        return null
    }

    // change Users current user to be the logged in user
    fun updateCurrentUser(context: Context, username: String): String? {
        var allUsersArr = users.allUsers

        // go through & find the correct user id for the user name
        var currentUserUserId = getUserIdFromUsername(allUsersArr, username)

        if (currentUserUserId != null) {
            var newCurrentUser = SingleUser(userId = currentUserUserId, username = username)
            users = Users(currentUser = newCurrentUser, usersArr = allUsersArr)

            Log.d("updateCurrentUser", "logged in username is now ${users.currentUser?.username} and the id is ${users.currentUser?.userId}")
        }

        return currentUserUserId
    }

    fun usercheck(input: String?): Boolean {
        return input != null
    }


    fun getAllUsers(context: Context){

        val url = serverUrl +"/getallusers"

        val getRequest = JsonObjectRequest(url,
            { response ->
                val usersReceived = try { response.getJSONArray("users") } catch (e: JSONException) { JSONArray() }

                Log.d("logUserIn", "user info received is ${usersReceived}")

                // parse info
                if(usersReceived.length() > 0){
                    var newUsersArr = mutableListOf<SingleUser>()

                    // loop over & create users arr
                    for (i in 0 until usersReceived.length()) {
                        var userObj = usersReceived[i] as JSONArray
                        var newUserId = userObj[0].toString()
                        var newUsername = userObj[1].toString()
                        var newUser = SingleUser(username = newUsername, userId = newUserId)
                        newUsersArr = (newUsersArr + newUser) as MutableList<SingleUser>
                    }

                    // TODO: Fix currentUser to not be set to null initially
                    users = Users(currentUser = null, usersArr=newUsersArr)

                    // Log.d("getAllUsers", "user arr is now ${users.allUsers.toString()}")
                    Log.d("getAllUsers", "user arr has been updated!")
                    }


            }, {  }
        )

        if (!this::queue.isInitialized) {
            queue = Volley.newRequestQueue(context)
        }

        queue.add(getRequest)
    }


}