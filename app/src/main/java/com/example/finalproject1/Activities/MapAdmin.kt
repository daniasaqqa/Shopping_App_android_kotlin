package com.example.finalproject1.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.finalproject1.R
import kotlinx.android.synthetic.main.activity_map_admin.*

class MapAdmin : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    var markerB=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_admin)
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


        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.uiSettings.isCompassEnabled=true
        mMap.uiSettings.isMyLocationButtonEnabled=true
        mMap.uiSettings.isRotateGesturesEnabled=true
        mMap.uiSettings.isTiltGesturesEnabled=true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.isMyLocationEnabled = true

        val c= mMap.isMyLocationEnabled

        // Add a marker in Sydney and move the camera


        // Map type
        mMap.mapType= GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapClickListener { point ->

            var lat = point.latitude.toString()
            var lng = point.longitude.toString()
            if(markerB){
                mMap.clear()
                markerB=false
            }
            if(markerB == false){
                mMap.addMarker(MarkerOptions().position(LatLng(lat.toDouble(),lng.toDouble())).title("Marker in city"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat.toDouble(),lng.toDouble()),16f))

                markerB=true
            }
          //  var s=LatLng(-31.529512, 34.451561)






            btn_save_location.setOnClickListener {
            var i = Intent(this, AddProduct::class.java)
            i.putExtra("lat", lat)
            i.putExtra("lng", lng)
            startActivity(i)
            finish()
            }
        }}


}
