package com.example.finalproject1.Activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalproject1.Adapters.ProductsHomeAdapter
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.Models.Users
import com.example.finalproject1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_user_pruched.*

class UserPruched : AppCompatActivity() {

    var prucheddata = mutableListOf<Users>()
    var fb = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pruched)


        val fUser= FirebaseAuth.getInstance().currentUser


        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(fUser!!.uid)
        ref.get()
            .addOnSuccessListener { task ->
                if (task != null){
                    val objectt= task.toObject(Users()::class.java)
                    prucheddata.add(objectt!!)
                    var data= task.data

                    rc_user_pruched.layoutManager = GridLayoutManager(this, 3)
                    rc_user_pruched.setHasFixedSize(true)
                    val womenadapter = ProductsHomeAdapter( objectt.productBuy as MutableList<ProductHomeModel>)
                    rc_user_pruched.adapter = womenadapter

                }else {
                    Toast.makeText(this,"Faild",Toast.LENGTH_LONG).show()
                }
            }





        btn_back_user_pruched.setOnClickListener {
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
    }




}
