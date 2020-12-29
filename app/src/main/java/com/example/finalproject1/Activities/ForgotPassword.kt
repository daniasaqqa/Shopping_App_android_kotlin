package com.example.finalproject1.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.finalproject1.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        firebaseAuth = FirebaseAuth.getInstance()
        btn_sen_forgot.setOnClickListener(View.OnClickListener {
            val email = et_email_forgot_pass.getText().toString()
            if (email == "") {
                Toast.makeText(
                    this@ForgotPassword,
                    " fialeds required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                firebaseAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPassword,
                            "Please check you email",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@ForgotPassword,
                                Login::class.java
                            )
                        )
                    } else {
                        val error = task.exception!!.message
                        Toast.makeText(
                            this@ForgotPassword,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}
