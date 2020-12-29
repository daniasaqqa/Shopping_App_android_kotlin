package com.example.finalproject1.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalproject1.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth= FirebaseAuth.getInstance()

        btn_done_change_pass.setOnClickListener {
            // call function
            changePassword()
        }
    }

    private fun  changePassword(){

        // insure the feild not empty
        if (et_current_password.text.isNotEmpty() && et_confirm_password.text.isNotEmpty() && et_new_password.text.isNotEmpty()){

            // insure if new password is equl confirm password
            if (et_new_password.text.toString().equals(et_confirm_password.text.toString())){

                // get current user
                val user = auth.currentUser
                // scan current user is not null and user email is not null
                if (user != null && user.email != null){

                    // get auth credential email provide
                    val credential=EmailAuthProvider
                        .getCredential(user.email!!,et_current_password.text.toString())

                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {it ->
                            if (it.isSuccessful){
                                Toast.makeText(this,"Re-Authentication success",Toast.LENGTH_SHORT).show()
                                user?.updatePassword(et_new_password.text.toString())
                                    ?.addOnCompleteListener { it ->
                                        if (it.isSuccessful){
                                            Toast.makeText(this,"Password Changed successfuly ",Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            startActivity(Intent(this,Login::class.java))
                                            finish()
                                        }
                                    }
                            }else{
                                Toast.makeText(this,"Re-Authentication faild",Toast.LENGTH_SHORT).show()
                            }
                        }

                }else{
                    startActivity(Intent(this,SignUp::class.java))
                    finish()
                }

            }else{
                Toast.makeText(this,"Missing matching",Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this,"Please Enter all the fields",Toast.LENGTH_SHORT).show()
        }
    }
}
