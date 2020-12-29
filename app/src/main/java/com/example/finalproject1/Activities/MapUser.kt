package com.example.finalproject1.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.finalproject1.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Double.parseDouble

class MapUser : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //latitude location
    var lat = 31.35

    // long location
    var lag = 34.57
    var pId:String=""
    //point location client
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_user)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        pId=intent.getStringExtra("prodlocId")!!

        //get Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.uiSettings.isCompassEnabled=true
        mMap.uiSettings.isMyLocationButtonEnabled=true
        mMap.uiSettings.isRotateGesturesEnabled=true
        mMap.uiSettings.isTiltGesturesEnabled=true
        // mMap.uiSettings.isMyLocationButtonEnabled = true
        // mMap.isMyLocationEnabled = true



        // Map type
        mMap.mapType= GoogleMap.MAP_TYPE_NORMAL

        // get location User
//         val sh=getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//
//        var lat=sh.getString("latUser","")!!
//        var lag=sh.getString("lagUser","")!!


        getLastLocation()
        // Add a marker in Sydney and move the camera
        val markeruser = LatLng( lat,lag)
        mMap.addMarker(MarkerOptions().position(markeruser).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markeruser,12f))


        var latProd=""
        var lngProd=""

        var fb=FirebaseFirestore.getInstance()
        var ref=fb.collection("product").document(pId)
        ref.get()
            .addOnSuccessListener {documentSnapshot ->
                if (documentSnapshot != null){
                    var data= documentSnapshot.data
                    latProd=data!!["localPlaceOne"] as String
                    lngProd=data!!["localPlaceTow"] as String



                    val markerProd = LatLng(parseDouble(latProd),parseDouble(lngProd))
                    mMap.addMarker(MarkerOptions().position(markerProd).title("Marker in Sydney"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerProd,12f))


                    mMap.addPolyline(
                        PolylineOptions()
                            .add(markeruser )
                            .add(markerProd)
                            .color(Color.RED)
                            .visible(true)
                    )
                }
                else{
//                    Toast.makeText(this,"Sorry",Toast.LENGTH_SHORT).show()
                }
            }







    }

    // Location functions
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        lat = location.latitude
                        lag = location.longitude

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            findViewById<TextView>(R.id.a).text = mLastLocation.latitude.toString()
            findViewById<TextView>(R.id.b).text = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }





}
