package edu.umich.aehill.reminiscetest

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

//fun queryForMostRecentTripID(context: Context, user_id: Int): String {
//    /*
//        SELECT `id`
//        FROM `table`
//        ORDER BY `date added` DESC
//        LIMIT 1
//     */
//
//    Log.e("TripPageView", "querying is happening")
//
//    var serverUrl = "https://34.75.243.151/getalltrips/$user_id"
//    var nFields = 7 // number of fields that each trip should have returned
//    var returnTripId = "2" // TODO: change?
//
//    Log.e("TripPageView", "User id is $user_id")
//    val queue = Volley.newRequestQueue(context)
//
//    val getRequest = JsonObjectRequest(serverUrl,
//        { response ->
//            val tripsReceived = try { response.getJSONArray("trips") } catch (e: JSONException) { JSONArray() }
//            Log.e("TripPageView", "trips received length is $tripsReceived.length()")
//            // get the first trip in the array
//            if(tripsReceived.length() > 0){
//                val tripEntry = tripsReceived[0] as JSONArray
//                if(tripEntry.length() == nFields){
//                    returnTripId = tripEntry[0].toString()  // TODO: is this the trip id? should be i think
//                    Log.e("TripPageView", "most recent completed trip query $returnTripId")
//                }
//                else{
//                    Log.e("TripPageView", "error with calling the most recently completed trip query")
//                }
//            }
//        }, {  }
//
//    )
//
//    queue.add(getRequest)
//
//    Log.e("TripPageView", "return trip id is $returnTripId")
//    return returnTripId
//}
