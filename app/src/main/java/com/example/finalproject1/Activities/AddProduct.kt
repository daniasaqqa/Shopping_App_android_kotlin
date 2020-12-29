package com.example.finalproject1.Activities



import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddProduct : AppCompatActivity(), OnMapReadyCallback {
    //var imageURI = ""
    var rateConutUser=ArrayList<String>()
    var rate=ArrayList<String>()
    val fb = FirebaseFirestore.getInstance()
    var markerB=true
    private lateinit var mMap: GoogleMap
    //latitude location
    var lat = ""

    // long location
    var lng = ""

    lateinit var progDialog:ProgressDialog
    private val PICK_IMAGE_REQUEST=71
    private var filePath:Uri?=null
    private var firebaseStore: FirebaseStorage?=null
    private var storageRefernce:StorageReference?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        // add Product Image
        firebaseStore = FirebaseStorage.getInstance()
        storageRefernce = FirebaseStorage.getInstance().reference
        img_add_product.setOnClickListener {
          launchGallery()

        }


        // get category admin adds and stored in spinner
        // فانجشين تجيب اسم التصنيف اللي بضيفة اليوزر و بتخزنو بالسبينر
        spinnerCategoriesName()

        btn_add_product_add_ptoduct.setOnClickListener {
            uploadImage()


        }


        // back to home
        btn_back_add_product.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
            finish()
        }



    }

    //Image function

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                img_add_product.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage(){
        progDialog= ProgressDialog(this)
        progDialog!!.setMessage("please wait ...")
        progDialog!!.show()
        if(filePath != null){
            val ref = storageRefernce?.child("images/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val imgUR=downloadUri.toString()
                    progDialog!!.dismiss()

                    rate.add("1")
                    rateConutUser.add("0")
                    // id my inputs
                    val nameProduct = product_name_add_product.text.toString()
                    val priceProduct = price_add_product.text.toString()
                    val typeProduct = type_add_product.selectedItem.toString()
                    val phoneproductuser = phone_add_product.text.toString()
                    val emailProduct = email_add_product.text.toString()
                    val descriptionProduct = description_add_product.text.toString()

                    // create database and Gives validity to all Function in the firestore
                    val fb = FirebaseFirestore.getInstance()

                    //create arraylist
                    val product: MutableMap<String, Any> = HashMap()
                    // enter my data to firebase
                    product["productName"] = nameProduct
                    product["productPrice"] = priceProduct
                    product["productType"] = typeProduct
                    product["productPhone"] = phoneproductuser
                    product["productEmail"] = emailProduct
                    product["productDescription"] = descriptionProduct
                    product["productImage"] = imgUR
                    product["localPlaceOne"] = lat
                    product["localPlaceTow"] = lng
                    product["SearchProductName"] = nameProduct.toLowerCase()
                    product["rateConutUser"]=rateConutUser
                    product["rateSystem"]=rate
                    product["productRating"]=0.0
                    product["productbuyCounter"]=0.0


                   // Toast.makeText(this, "lat: $lat \n lng: $lng", Toast.LENGTH_LONG).show()

                    // add category or type(collection) main object or class
                    fb.collection("product")
                        // add items
                        .add(product)
                        .addOnSuccessListener {
                            //  Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Add Error", Toast.LENGTH_LONG).show()
                        }
                    startActivity(Intent(this, Main2Activity::class.java))
                    finish()

                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun spinnerCategoriesName() {
        val spL: MutableList<String> = ArrayList()
        fb.collection("category")
            .get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result!!) {
                        val id = document.id
                        val data = document.data
                        val catyName = data["categorytName"] as String?
                        spL.add(catyName!!)
                    }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spL)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                type_add_product.adapter = adapter

            }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!


        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.isMyLocationEnabled = true





        // Map type
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapClickListener { point ->

            lat = point.latitude.toString()
            lng = point.longitude.toString()
            if (markerB) {
                mMap.clear()
                markerB = false
            }
            if (markerB == false) {
                mMap.addMarker(
                    MarkerOptions().position(LatLng(lat.toDouble(), lng.toDouble()))
                        .title("Marker in city")
                )
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lat.toDouble(),
                            lng.toDouble()
                        ), 16f
                    )
                )

                markerB = true
            }


        }}}

