package com.example.finalproject1.Activities


import android.accessibilityservice.GestureDescription
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject1.Fragments.AddFragment
import com.example.finalproject1.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class AddCategory : AppCompatActivity() {
    var imageURI = ""

    lateinit var progDialog: ProgressDialog
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri?=null
    private var firebaseStore: FirebaseStorage?=null
    private var storageRefernce:StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)



        // add Category Image
        firebaseStore = FirebaseStorage.getInstance()
        storageRefernce = FirebaseStorage.getInstance().reference
        category_add_img.setOnClickListener {
            launchGallery()
        }


        //add data to firebase & intent to mainActivity
        btn_add_category.setOnClickListener {
            uploadImage()

        }


        //intent to main 2 activity
        btn_back_add_category.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
            // ** supportFragmentManager.beginTransaction().replace( R.id.mainContainer,AddFragment())
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
                category_add_img.setImageBitmap(bitmap)
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

            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val downloadUri = it.result
                    val imgUR=downloadUri.toString()
                    progDialog!!.dismiss()


                    // id my inputs
                    val nameCategory = et_category_name_add.text.toString()


                    // create database and Gives validity to all Function in the firestore
                    val fb = FirebaseFirestore.getInstance()

                    //create arraylist
                    val category: MutableMap<String, Any> = HashMap()
                    // enter my data to firebasedata
                    category["categorytName"] = nameCategory
                    category["categoryImage"] = imgUR
                    category["nameSearch"] = nameCategory.toLowerCase()



                    // add category or type(collection) main object or class
                    fb.collection("category")
                        // add items
                        .add(category)
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
}
