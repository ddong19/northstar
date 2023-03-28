package edu.umich.aehill.reminiscetest

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import org.json.JSONException
import edu.umich.aehill.reminiscetest.Trip

object TripStore {
    var currentTrip = Trip()
    private lateinit var queue: RequestQueue
    private const val serverUrl = "https://34.75.243.151"

    // for updating the current trip page
    fun updateCurrentTrip(context: Context, userId: Int) {

        Log.d("TripStore/updateCurrentTrip", "update current trip called")

        val nFields = 7

        val url = serverUrl+"/getalltrips/"+userId

        Log.d("TripStore/updateCurrentTrip", url)

        val getRequest = JsonObjectRequest(url,
            { response ->
                val tripsReceived = try { response.getJSONArray("trips") } catch (e: JSONException) { JSONArray() }
                if(tripsReceived.length() > 0){
                    val tripEntry = tripsReceived[0] as JSONArray // first trip is most recent
                    if(tripEntry.length() == nFields){

                        // update currentTrip

                        currentTrip = Trip(tripId = tripEntry[0].toString(), userId = tripEntry[1].toString(),
                        destination = tripEntry[2].toString(), startDate = tripEntry[3].toString(), endDate = tripEntry[4].toString(),
                        ownerUsername = tripEntry[5].toString(), description = tripEntry[5].toString())

                        Log.d("TripStore/updateCurrentTrip", "current trip id has been updated to " + currentTrip.tripId.toString())

                        // trip id, user id, destination, start date, end date, username, description
                        // [209, 3, "cancun", "03162023", "03192023", "alannaemmrie", "test trip description"]

                        // get the images for this trip also

                        getImagesForCurrentTrip(context, currentTrip.tripId)

                    }
                    else{
                        Log.e("TripStore/updateCurrentTrip", "error with calling the most recently completed trip query")
                    }
                }

            }, {  }
        )

        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(getRequest)
    }

    // TODO: write func for updating the current trip location information

    // for updating the images for the current trip page
    fun getImagesForCurrentTrip(context: Context, tripId: String?){

        Log.d("getImagesForCurrentTrip", "getImagesForCurrentTrip called")

        val url = serverUrl+"/gettripimages/"+tripId
        Log.d("getImagesForCurrentTrip", url)

        val getRequest = JsonObjectRequest(
            url,
            { response ->
                val imagesReceived = try { response.getJSONArray("images") } catch (e: JSONException) { JSONArray() }
                if (imagesReceived.length() > 0){
                    var imageURIs = mutableListOf<TripImage>()

                    for (i in 0 until imagesReceived.length()) {
                        val image = imagesReceived[i] as JSONArray
                        val tripImage = TripImage(imageId = image[0].toString(),
                            tripId = image[1].toString(), coords = image[2].toString(),
                            URI = image[3].toString())
                        Log.e("getImagesForCurrentTrip", image.toString())
                        if (image.length() == 3 || image.length() == 4) {
                            // imageURIs = (imageURIs + image[3].toString()) as MutableList<String>
                            imageURIs = (imageURIs + tripImage) as MutableList<TripImage>
                        } else {
                            Log.e("getImagesForCurrentTrip", "Received unexpected number of fields: " + image.length().toString() + " instead of 3")
                        }

                        // set the images to be the trip images for current trip
                        currentTrip.imageURIs = imageURIs // (hopefully this works ??)

                        Log.d("getImagesForCurrentTrip", "Current trip images have been updated")
                        Log.d("getImagesForCurrentTrip", "size of current trip images is " + currentTrip.imageURIs!!.size)


                    }

                }

            }, {  }

        )
        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(getRequest)
    }
}




