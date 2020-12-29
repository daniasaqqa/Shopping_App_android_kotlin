package com.example.finalproject1.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.finalproject1.Models.Users
import com.example.finalproject1.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        btn_login.setOnClickListener {
            var autherdania: FirebaseAuth? = FirebaseAuth.getInstance()
            autherdania!!.signInWithEmailAndPassword(
                et_email_login.text.toString(),
                et_password_login.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var i = Intent(this, Main2Activity::class.java)
                        startActivity(i)
                        finish()
                        Toast.makeText(this, "Success login", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->

                    Toast.makeText(this, "felid login", Toast.LENGTH_SHORT).show()

                }


        }




        tv_sign_up_login_screen.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }
        tv_forgot_pass_login_screen.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
            finish()
        }


    }


}
