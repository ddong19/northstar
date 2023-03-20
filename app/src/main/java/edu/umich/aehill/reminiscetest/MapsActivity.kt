package edu.umich.aehill.reminiscetest

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


internal class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val points = listOf(
            LatLng(42.2808, 83.7430),
            LatLng(43.6532, 79.3832),
            LatLng(51.1215, 114.0076),
            LatLng(51.0899, 115.3441),
            LatLng(51.1784, 115.5708)
        )

        // Create an empty list to hold the marker objects
        val markers = mutableListOf<Marker>()

        // Add a marker for each LatLng point and add it to the markers list
        for (point in points) {
            val marker = googleMap.addMarker(MarkerOptions().position(point))
            if (marker != null) {
                markers.add(marker)
            }
        }

        // Create a polylineOptions object to customize the appearance of the polyline
        val polylineOptions = PolylineOptions()
            .color(Color.BLUE)
            .width(5f)
            .geodesic(true)

        // Add each point to the polylineOptions object
        for (point in points) {
            polylineOptions.add(point)
        }

        // Add the polyline to the map using the polylineOptions object
        val polyline = googleMap.addPolyline(polylineOptions)

        // Set the bounds of the map to include all the markers and the polyline
        val builder = LatLngBounds.Builder()
        for (marker in markers) {
            builder.include(marker.position)
        }
        builder.include(polyline.points[0])
        builder.include(polyline.points[polyline.points.size - 1])
        val bounds = builder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }
    }
//    fun ok(){
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        LocationServices.getFusedLocationProviderClient(applicationContext)
//            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    lat = it.result.latitude
//                    long = it.result.longitude
//                } else {
//                    Log.e("PostActivity getFusedLocation", it.exception.toString())
//                }
//            }
//    }

