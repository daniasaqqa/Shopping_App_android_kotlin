package com.example.finalproject1.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.finalproject1.R
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    lateinit var topAnimation: Animation
    lateinit var bottomAnimation: Animation
    lateinit var middleAnimation: Animation
    var imageURI = ""
   var lngUser=""
    var latUser=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        //Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation)
        //bottom antimation
        view_sign_up.animation = bottomAnimation
        et_username_signup.animation = bottomAnimation
        et_email_sign_up.animation = bottomAnimation
        et_mobile_sign_up.animation = bottomAnimation
        et_password_sign_up.animation = bottomAnimation
        btn_sign_up.animation = bottomAnimation
        view2_sign_up.animation = bottomAnimation
        tv_login.animation = bottomAnimation
        textView3.animation = bottomAnimation
        //middle animation
        tv_Sign_up_signup_screen.animation = middleAnimation

        // top animation
        imageView_girl.animation = topAnimation
        textView_home.animation = topAnimation
        tv_shopping.animation = topAnimation



        // call function and save&ensure the data user and go to login screen
        btn_sign_up.setOnClickListener {

            // edit text input
//            val username = et_username_signup.text.toString()
//            val emaild = et_email_sign_up.text.toString()
//            val mobile = et_mobile_sign_up.text.toString()
//            val passwordd = et_password_sign_up.text.toString()

            signUp(et_username_signup.text.toString(), et_email_sign_up.text.toString(), et_mobile_sign_up.text.toString(), et_password_sign_up.text.toString())

        }

        // go to Login Screen if you can account
        tv_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    fun signUp(username: String, userEmail: String, phone: String, passwordd: String) {
        val autherdania: FirebaseAuth? = FirebaseAuth.getInstance()

        autherdania!!.createUserWithEmailAndPassword(userEmail, passwordd)
            .addOnSuccessListener {
                val fireUser = FirebaseAuth.getInstance().currentUser

                var fb = FirebaseFirestore.getInstance()
                val hashMap:MutableMap<String, Any> = HashMap()

                hashMap["username"] = username
                hashMap["userMobile"] = phone
                hashMap["userEmail"] = userEmail
                hashMap["aboutMe"]=""
                hashMap["userImage"]=""
                hashMap["productBuy"] =  ArrayList<Any>()


                var ref = fb.collection("users").document(fireUser!!.uid)
                    ref.set(hashMap)
                    .addOnSuccessListener {
                       // Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show()
                        val i = Intent(this@SignUp, Login::class.java)
                        startActivity(i)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Add Error", Toast.LENGTH_LONG).show()
                    }



            }
            .addOnFailureListener {e->
                Log.d("da21" , "f" , e)

            }


    }





}



