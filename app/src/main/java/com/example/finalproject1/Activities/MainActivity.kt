package com.example.finalproject1.Activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.finalproject1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var topAnimation: Animation
    lateinit var bottomAnimation: Animation
    lateinit var middleAnimation: Animation
    var firebase:FirebaseUser?=null
  //  val PERMISSION_ID = 42

    //point location client
   // lateinit var mFusedLocationClient: FusedLocationProviderClient

    //latitude location
    var lat = 0.0

    // long location
    var lag = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        //get Location
      //  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
      //  getLastLocation()

        topAnimation=AnimationUtils.loadAnimation(this,R.anim.top_animation)
        bottomAnimation =AnimationUtils.loadAnimation(this,R.anim.bottom_animation)
        middleAnimation=AnimationUtils.loadAnimation(this,R.anim.middle_animation)

        first_line.animation=topAnimation
        second_line.animation=topAnimation
        third_line.animation=topAnimation
        fourth_line.animation=topAnimation
        fifth_line.animation=topAnimation
        sixth_line.animation=topAnimation

        logo_splash.animation=middleAnimation
        name_dania.animation=bottomAnimation

        val background=object :Thread(){
            override fun run() {
                try {
                    Thread.sleep(3000)
                        val intt= Intent(baseContext,
                            SignUp::class.java)
                        startActivity(intt)
                    finish()
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        }
        background.start()


    }

    override fun onStop() {
        super.onStop()

//        val sh=getSharedPreferences("MyPref",Context.MODE_PRIVATE)
//        val editor=sh.edit()
//        editor.putString("lagUser",lag.toString())
//        editor.putString("latUser",lat.toString())
//        val e=editor.commit()
//        if(e){
//            Toast.makeText(this,"success",Toast.LENGTH_LONG).show()
//        }else{
//            Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
//            startActivity(Intent(this,SignUp::class.java))
//        }
        firebase=FirebaseAuth.getInstance().currentUser
        if(firebase != null ){
            val i= Intent(this,Main2Activity::class.java)
            startActivity(i)
            finish()
        }else{
            val i= Intent(this,SignUp::class.java)
            startActivity(i)
            finish()
        }

    }



    // Location functions
//    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//
//                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
//                    var location: Location? = task.result
//                    if (location == null) {
//                        requestNewLocationData()
//                    } else {
//                        lat = location.latitude
//                        lag = location.longitude
//
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }

//    @SuppressLint("MissingPermission")
//    private fun requestNewLocationData() {
//        var mLocationRequest = LocationRequest()
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval = 0
//        mLocationRequest.fastestInterval = 0
//        mLocationRequest.numUpdates = 1
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        mFusedLocationClient!!.requestLocationUpdates(
//            mLocationRequest, mLocationCallback,
//            Looper.myLooper()
//        )
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            var mLastLocation: Location = locationResult.lastLocation
//            findViewById<TextView>(R.id.a).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.b).text = mLastLocation.longitude.toString()
//        }
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//
//    private fun checkPermissions(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
//            PERMISSION_ID
//        )
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        if (requestCode == PERMISSION_ID) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                getLastLocation()
//            }
//        }
//    }
//

}
