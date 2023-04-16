package edu.umich.aehill.reminiscetest

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

object SpotifyStore{

    var currentSpotify = Spotify()
    private lateinit var queue: RequestQueue
    private const val serverUrl = "https://34.75.243.151"

    fun updateCurrentSpotify(context: Context, playlistId: String?) {
        Log.d("updateCurrentSpotify", "playlist id is ${playlistId}")
        val url = SpotifyStore.serverUrl +"/getspotifyplaylist/"+playlistId

        val getRequest = JsonObjectRequest(url,
            { response ->
                val spotifyReceived = try { response.getJSONObject("data") } catch (e: JSONException) { JSONObject() }

                Log.d("SpotifyStore/updateCurrentSpotify", "spotify received is ${spotifyReceived.toString()}")

                var playlistName = spotifyReceived.getString("name") // "name" // spotifyReceived["name"] // data[7] as JSONArray
                var tracksObj = spotifyReceived.getJSONObject("tracks")
                var tracksArr = tracksObj.getJSONArray("items")

                Log.d("SpotifyStore/updateCurrentSpotify", playlistName.toString())

                Log.d("SpotifyStore/updateCurrentSpotify", "playlist name is ${playlistName}")

                // loop over tracks
                var playlistSongs = mutableListOf<SpotifySong>()

                if(tracksArr.length() > 0) {
                    for (i in 0 until tracksArr.length()) {
                        var trackObj = tracksArr[i] as JSONObject

                        Log.d("getCurrentSpotify", "single track object looks like: ${trackObj}")

                        var track = trackObj.getJSONObject("track")

                        Log.d("getCurrentSpotify", "track is ${track}")

                        var trackName = track.getString("name")
                        var currentSong = SpotifySong(artist="alanna hill", name=trackName, songImageURI=null)
                        playlistSongs = (playlistSongs + currentSong) as MutableList<SpotifySong>
                        currentSpotify.songs = playlistSongs
                    }
                }

                currentSpotify = Spotify(playlistName = playlistName.toString(), songs = playlistSongs)
                Log.d("updateCurrentSpotify", "songs are ${currentSpotify.songs}")


            }, {  }
        )

        if (!this::queue.isInitialized) {
            SpotifyStore.queue = Volley.newRequestQueue(context)
        }
        SpotifyStore.queue.add(getRequest)
    }





}