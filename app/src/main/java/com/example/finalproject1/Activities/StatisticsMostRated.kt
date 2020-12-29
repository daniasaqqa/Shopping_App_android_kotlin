package com.example.finalproject1.Activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalproject1.Adapters.ProductsHomeAdapter
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_statistics_most_rated.*

class StatisticsMostRated : AppCompatActivity() {
    var fb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_most_rated)

        productData()


        btn_back_rate_stati.setOnClickListener {
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
    }

    private fun productData(){
        val productData= mutableListOf<ProductHomeModel>()
        fb.collection("product").whereGreaterThanOrEqualTo("productRating",1 )
            .orderBy("productRating", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        val id=document.id
                        val data=document.data
                        val productname = data["productName"] as String
                        val productImage = data["productImage"] as String
                        productData.add(
                            ProductHomeModel(
                                id,
                                productImage,
                                productname

                            )
                        )
                    }
                    rc_statistics_most_rated.layoutManager = GridLayoutManager(this,3)
                    rc_statistics_most_rated.setHasFixedSize(true)
                    val adapter = ProductsHomeAdapter(productData)
                    rc_statistics_most_rated.adapter = adapter
                }

            }



    }

}

