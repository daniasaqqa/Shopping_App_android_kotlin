package com.example.finalproject1.Activities


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject1.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_edit_product.*
import java.io.IOException
import java.lang.Integer.parseInt
import java.lang.reflect.TypeVariable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditProduct : AppCompatActivity() {
   // var imageURI = ""
   var imageURI = ""
    val fb = FirebaseFirestore.getInstance()
    lateinit var progDialog: ProgressDialog
    private val PICK_IMAGE_REQUEST=71
    private var filePath:Uri?=null
    private var firebaseStore: FirebaseStorage?=null
    private var storageRefernce: StorageReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

       //  cId=intent!!.getStringExtra("catyId")!!


        firebaseStore = FirebaseStorage.getInstance()
        storageRefernce = FirebaseStorage.getInstance().reference
        // add image
        img_edit_product.setOnClickListener {
            launchGallery()


        }


        // intent to product details activity
        btn_back_edit_product.setOnClickListener {
            startActivity(Intent(this,ProductDetails::class.java))
        }


        val pId:String=intent!!.getStringExtra("prodId")!!


        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var  data=documentSnapshot.data
                    val nameProd = data!!["productName"] as String
                    val priceProd= data["productPrice"] as String
                    val phoneProd=data["productPhone"] as String
                    val emailProd=data["productEmail"] as String
                    val type = data["productType"] as String
                    val descripProd=data["productDescription"] as String
                    val img=data["productImage"] as String
                    //  val sp=data["productType"] as String
                    product_name_edit_product.setText(nameProd)
                    price_edit_product.setText(priceProd)
                    description_edit_product.setText(descripProd)
                    phone_edit_product.setText(phoneProd)
                    email_edit_product.setText(emailProd)
                    spinnerCategoriesName(type)
                    // img_edit_product.setImageURI(Uri.parse(img))
                    Picasso.get().load(img).into(img_edit_product)

                    Log.e("dna",nameProd)}
                else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this,"$exception",Toast.LENGTH_LONG).show()
            }


        //sp()

        btn_edit_product_edit_ptoduct.setOnClickListener {

            uploadImage()

        }

    }

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
                img_edit_product.setImageBitmap(bitmap)
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

                    // id my inputs
                    val nameProduct =product_name_edit_product.text.toString()
                    val priceProduct = price_edit_product.text.toString()
                    // val typeProduct = type_edit_product.selectedItem.toString()
                    val phoneproductuser = phone_edit_product.text.toString()
                    val emailProduct = email_edit_product.text.toString()
                    val descriptionProduct = description_edit_product.text.toString()

                    // create database and Gives validity to all Function in the firestore
                    val fb = FirebaseFirestore.getInstance()
                    val pId:String=intent!!.getStringExtra("prodId")!!
                    var ref = fb.collection("product").document(pId)

                    //create arraylist
                    val product: MutableMap<String, Any> = HashMap()
                    // enter my data to firebase
                    product["productName"] = nameProduct
                    product["productPrice"] = priceProduct
                    //  product["productType"] = typeProduct
                    product["productPhone"] = phoneproductuser
                    product["productEmail"] = emailProduct
                    product["productDescription"] = descriptionProduct
                    product["productImage"] = imgUR
                    product["SearchProductName"] = nameProduct.toLowerCase()



                    fb.collection("product")
                    // add items
                    ref.update(product)
                        .addOnSuccessListener {
                            //  Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Edit Error", Toast.LENGTH_LONG).show()
                        }
                    startActivity(Intent(this, Main2Activity::class.java))
                    finish()

                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
           // Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            // id my inputs
            val nameProduct =product_name_edit_product.text.toString()
            val priceProduct = price_edit_product.text.toString()
             val typeProduct = type_edit_product.selectedItem.toString()
            val phoneproductuser = phone_edit_product.text.toString()
            val emailProduct = email_edit_product.text.toString()
            val descriptionProduct = description_edit_product.text.toString()

            // create database and Gives validity to all Function in the firestore
            val fb = FirebaseFirestore.getInstance()
            val pId:String=intent!!.getStringExtra("prodId")!!
            var ref = fb.collection("product").document(pId)

            //create arraylist
            val product: MutableMap<String, Any> = HashMap()
            // enter my data to firebase
            product["productName"] = nameProduct
            product["productPrice"] = priceProduct
            product["productType"] = typeProduct
            product["productPhone"] = phoneproductuser
            product["productEmail"] = emailProduct
            product["productDescription"] = descriptionProduct
            product["SearchProductName"] = nameProduct.toLowerCase()



            fb.collection("product")
            // add items
            ref.update(product)
                .addOnSuccessListener {
                    //  Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Edit Error", Toast.LENGTH_LONG).show()
                }
            startActivity(Intent(this, Main2Activity::class.java))
            finish()

        }
    }

    private fun spinnerCategoriesName(catyName:String) {
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
                type_edit_product.adapter = adapter

                type_edit_product.setSelection(adapter.getPosition(catyName))
            }
    }


}
