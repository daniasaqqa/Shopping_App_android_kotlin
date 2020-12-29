package com.example.finalproject1.Activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalproject1.Adapters.CategoryProfileAdapter
import com.example.finalproject1.Models.CategoryModel
import com.example.finalproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_category_profile.*

class CategoryProfile : AppCompatActivity() {

    var fb = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_profile)


        // call function
        categoryData()


        // back to main2activity.
        btn_back_category_profile.setOnClickListener {
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }

    }

    private fun categoryData(){


        // Products in category profile
        val catProfData= mutableListOf<CategoryModel>()
        fb.collection("category")
            .get()
            .addOnCompleteListener {querySnapshot->
                if(querySnapshot.isSuccessful){
                    for (document in querySnapshot.result!!){
                        // get catefory id
                        val id=document.id

                        // array insert  gat category data
                        val data=document.data

                        // get feild name category data and store to variable
                        val productName = data["categorytName"] as String?
                        val productImage = data["categoryImage"] as String?

                        //add category data to array list
                        catProfData.add(
                            CategoryModel(id,
                                productImage,
                                productName

                            )
                        )
                    }
                    //add adapter to my recycle
                    // number col & layout type grid recycle
                    rc_categories_profile.layoutManager = GridLayoutManager(this,1)
                    rc_categories_profile.setHasFixedSize(true)
                    val Adapter = CategoryProfileAdapter(catProfData)
                    rc_categories_profile.adapter = Adapter
                }
            }

    }
    

}
