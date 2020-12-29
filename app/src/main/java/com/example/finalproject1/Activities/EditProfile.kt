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
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject1.Models.Users
import com.example.finalproject1.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.IOException
import java.util.*


class EditProfile : AppCompatActivity() {

    val fireUser = FirebaseAuth.getInstance().currentUser

    var ref:StorageReference?=null
   // var imageURI = ""
    lateinit var progDialog: ProgressDialog
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
   // private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        storageReference = FirebaseStorage.getInstance().reference
        img_edit_profile.setOnClickListener {
            //add Image by pick image library
            launchGallery()
        }


        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(fireUser!!.uid)

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {

                    var data = documentSnapshot.data
                    val username = data!!["username"] as String
                    val email = data["userEmail"] as String
                    val phone = data["userMobile"] as String
                    val aboutme = data["aboutMe"] as String
                    val img = data["userImage"] as String
                    edit_name_edit_profile.setText(username)
                    edit_email_edit_profile.setText(email)
                    edit_phone_edit_profile.setText(phone)
                    edit_about_me_edit_profile.setText(aboutme)
                  //  img_edit_profile.setImageURI(Uri.parse(img))
                    if (img.isNotEmpty()) {
                        Picasso.get().load(img).into(img_edit_profile)
                    }else{

                       img_edit_profile.setImageResource(R.drawable.img_slide_tow)
                    }

                } else {
                    Toast.makeText(this, "Failer", Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }






        btn_back_edit_profile.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
            finish()
        }

        btn_Save_edit_profile.setOnClickListener {

            uploadImage()
            updateEmail()

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
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                img_edit_profile.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        progDialog = ProgressDialog(this)
        progDialog!!.setMessage("please wait ...")
        progDialog!!.show()
        if (filePath != null) {
           val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
           val uploadTask = ref?.putFile(filePath!!)


               val urlTask= uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
         task.exception?.let {
                            throw it
                       }
                 }
                    return@Continuation ref!!.downloadUrl
                })?.addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       val downloadUri = task.result
                       val imgUR=downloadUri.toString()
                        progDialog!!.dismiss()
                       val username= edit_name_edit_profile.text.toString()
                       val userEmail = edit_email_edit_profile.text.toString()
                       val userMobile = edit_phone_edit_profile.text.toString()
                       val aboutMe = edit_about_me_edit_profile.text.toString()

                       val fb = FirebaseFirestore.getInstance()
                        val ref = fb.collection("users").document(fireUser!!.uid)
                        val up = HashMap<String, Any>()
                        //var dataa:MutableMap<String,Any>
                        up["username"] = username
                        up["userEmail"] = userEmail
                        up["userMobile"] = userMobile
                        up["aboutMe"] = aboutMe
                       up["userImage"] = imgUR

                       fb.collection("users")
                        ref.update(up)
                            .addOnSuccessListener {
                              //  Toast.makeText(this, "Success Edit", Toast.LENGTH_LONG).show()

                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
                            }
                       startActivity(Intent(this, Main2Activity::class.java))
                       finish()


                    }

                }?.addOnFailureListener {

                }
        } else {
         //  Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            val username= edit_name_edit_profile.text.toString()
            val userEmail = edit_email_edit_profile.text.toString()
            val userMobile = edit_phone_edit_profile.text.toString()
            val aboutMe = edit_about_me_edit_profile.text.toString()

            val fb = FirebaseFirestore.getInstance()
            val ref = fb.collection("users").document(fireUser!!.uid)
            val up = HashMap<String, Any>()
            //var dataa:MutableMap<String,Any>
            up["username"] = username
            up["userEmail"] = userEmail
            up["userMobile"] = userMobile
            up["aboutMe"] = aboutMe
          //  up["userImage"] = imgUR

            fb.collection("users")
            ref.update(up)
                .addOnSuccessListener {
                    //  Toast.makeText(this, "Success Edit", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
                }
            startActivity(Intent(this, Main2Activity::class.java))
            finish()

        }
    }

    private fun updateEmail(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
        val credential = EmailAuthProvider
            .getCredential(user.email!!,user.email!!)
        user.reauthenticate(credential)
            .addOnCompleteListener {it ->
                if (it.isSuccessful){
                    val user=FirebaseAuth.getInstance().currentUser
                    if (user != null){
                    user.updateEmail(edit_email_edit_profile.text.toString())
                        .addOnCompleteListener { it ->
                            if (it.isSuccessful){
                                Toast.makeText(this,"update email successfully",Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this,"update email failed",Toast.LENGTH_SHORT).show()
                            }

                        }


                    }
                }

            }
    }else{
            Toast.makeText(this,"errorss",Toast.LENGTH_SHORT).show()
        }
    }

}
