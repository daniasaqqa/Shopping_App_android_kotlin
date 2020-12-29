package com.example.finalproject1.Activities


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject1.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_edit_category.*
import kotlinx.android.synthetic.main.activity_edit_product.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class EditCategory : AppCompatActivity() {

    var imageURI = ""
    val fb = FirebaseFirestore.getInstance()
    lateinit var progDialog: ProgressDialog
    private val PICK_IMAGE_REQUEST=71
    private var filePath:Uri?=null
    private var firebaseStore: FirebaseStorage?=null
    private var storageRefernce: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        val cId:String=intent!!.getStringExtra("catyId")!!


        // add image
        firebaseStore = FirebaseStorage.getInstance()
        storageRefernce = FirebaseStorage.getInstance().reference
        category_edit_img.setOnClickListener {
            launchGallery()


        }






        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("category").document(cId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var  data=documentSnapshot.data
                    val nameCat = data!!["categorytName"] as String

                    val img=data["categoryImage"] as String
                    et_category_name_edit.setText(nameCat)

                   // category_edit_img.setImageURI(Uri.parse(img))
                    // insure img in my collection is not null
                    if(img.isNotEmpty()){
                        // عرض الصورة
                    Picasso.get().load(img).into(category_edit_img)
                    }else{
                        // default image
                        category_edit_img.setImageResource(R.drawable.img_slide_tow)
                    }

                  }
                else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this,"$exception",Toast.LENGTH_LONG).show()
            }




       edit_catedory_save.setOnClickListener {
          uploadImage()

       }







        btn_back_edit_category.setOnClickListener {
            startActivity(Intent(this,CategoryProfile::class.java))
            finish()
        }


    }


    private fun launchGallery() {
        val intent = Intent()
        // type collection image (jpg,png,gif ...)
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

                category_edit_img.setImageBitmap(bitmap)
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
                    val nameCat =et_category_name_edit.text.toString()


                    // create database and Gives validity to all Function in the firestore
                    val fb = FirebaseFirestore.getInstance()
                    val pId:String=intent!!.getStringExtra("catyId")!!
                    var ref = fb.collection("category").document(pId)

                    //create arraylist
                    val up: MutableMap<String, Any> = HashMap()
                    // enter my data to firebase
                    up["categorytName"] = nameCat

                    up["nameSearch"] = nameCat.toLowerCase()
                    up["categoryImage"]=imgUR



                    // Toast.makeText(this, "lat: $lat \n lng: $lng", Toast.LENGTH_LONG).show()

                    // add category or type(collection) main object or class
                    fb.collection("category")
                    // add items
                    ref.update(up)
                        .addOnSuccessListener {
                            //  Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Edit Error", Toast.LENGTH_LONG).show()
                        }
                    startActivity(Intent(this, CategoryProfile::class.java))
                    finish()

                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
           // Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            // id my inputs
            val nameCat =et_category_name_edit.text.toString()


            // create database and Gives validity to all Function in the firestore
            val fb = FirebaseFirestore.getInstance()
            val pId:String=intent!!.getStringExtra("catyId")!!
            var ref = fb.collection("category").document(pId)

            //create arraylist
            val up: MutableMap<String, Any> = HashMap()
            // enter my data to firebase
            up["categorytName"] = nameCat

            up["nameSearch"] = nameCat.toLowerCase()
          //  up["categoryImage"]=imgUR



            // Toast.makeText(this, "lat: $lat \n lng: $lng", Toast.LENGTH_LONG).show()

            // add category or type(collection) main object or class
            fb.collection("category")
            // add items
            ref.update(up)
                .addOnSuccessListener {
                    //  Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Edit Error", Toast.LENGTH_LONG).show()
                }
            startActivity(Intent(this, CategoryProfile::class.java))
            finish()
        }
    }

}
