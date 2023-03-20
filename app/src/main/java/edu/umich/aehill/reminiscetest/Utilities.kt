package edu.umich.aehill.reminiscetest

import android.app.Application
import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

public class Global : Application() {
    companion object {
        @JvmField
        var currentTripID: String = ""
        var currentTripLocation: String = " "
    }
}

fun queryForMostRecentTripID(context: Context, user_id: Int): String {
    /*
        SELECT `id`
        FROM `table`
        ORDER BY `date added` DESC
        LIMIT 1
     */

    var serverUrl = "https://34.75.243.151/getalltrips/$user_id"
    var nFields = 7 // number of fields that each trip should have returned
    var returnTripId = "2" // TODO: change?

    Log.e("TripPageView", "User id is $user_id")
    val queue = Volley.newRequestQueue(context)

    val getRequest = JsonObjectRequest(serverUrl,
        { response ->
            val tripsReceived = try { response.getJSONArray("trips") } catch (e: JSONException) { JSONArray() }
            Log.e("TripPageView", "trips received length is $tripsReceived.length()")
            // get the first trip in the array
            if(tripsReceived.length() > 0){
                val tripEntry = tripsReceived[0] as JSONArray
                if(tripEntry.length() == nFields){
                    returnTripId = tripEntry[0].toString()
                    Global.currentTripID = returnTripId
                    Log.e("TripPageView", "most recent completed trip query $returnTripId")
                }
                else{
                    Log.e("TripPageView", "error with calling the most recently completed trip query")
                }
            }

            // get current trip location
            var serverUrl = "https://34.75.243.151/gettripdata/${Global.currentTripID}"
            Log.e("utilities", "server url is $serverUrl")

            val queue = Volley.newRequestQueue(context)


            val getRequest = JsonObjectRequest(serverUrl,
                { response ->
                    val tripReceived = try { response.getJSONArray("trip_data") } catch (e: JSONException) { JSONArray() }
                    Log.d("trip received", "$tripReceived")
                    val tripDetails = tripReceived[0] as JSONArray
                    Log.d("trip details", "$tripDetails")

                    Global.currentTripLocation = tripDetails[2].toString()
                    Log.d("trip location", "${Global.currentTripLocation}")
                }, {  }

            )
            queue.add(getRequest)

        }, {  }

    )

    queue.add(getRequest)
    return returnTripId
}

fun getAllImagesForTrip(context: Context, tripId: String?): ArrayList<String> {

    var serverUrl = "https://34.75.243.151/gettripimages/$tripId"
    Log.e("utilities", "server url is $serverUrl")

    val queue = Volley.newRequestQueue(context)

    var imageURIs = arrayListOf<String>()
    Log.e("Utilities", "sending get request to gettripimages")

    val getRequest = JsonObjectRequest(serverUrl,
        { response ->
            val imagesReceived = try { response.getJSONArray("images") } catch (e: JSONException) { JSONArray() }
            if (imagesReceived.length() > 0){
                for (i in 0 until imagesReceived.length()) {
                    val image = imagesReceived[i] as JSONArray
                    if (image.length() == 3 || image.length() == 4) {
                        imageURIs = (imageURIs + image[2].toString()) as ArrayList<String>
                        Log.e("utilities", imageURIs.toString())
                    } else {
                        Log.e("getAllImagesForTrip", "Received unexpected number of fields: " + image.length().toString() + " instead of 3")
                    }
                }

            }

        }, {  }

    )

    queue.add(getRequest)

    Log.e("utilities", imageURIs.toString())
    return imageURIs


}

