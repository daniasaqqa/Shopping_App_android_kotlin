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
import kotlinx.android.synthetic.main.activity_statistics_buy_product.*

class StatisticsBuyProduct : AppCompatActivity() {

    var fb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_buy_product)




        productData()

        btn_back_buy_statis.setOnClickListener {
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
    }
    private fun productData(){
        val productData= mutableListOf<ProductHomeModel>()
        fb.collection("product").whereGreaterThanOrEqualTo("productbuyCounter",1 )
            .orderBy("productbuyCounter", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        val id=document.id
                        val data=document.data
                        val productName = data["productName"] as String?
                        val productImage = data["productImage"] as String?
                        productData.add(
                            ProductHomeModel(
                                id,
                                productImage,
                                productName

                            )
                        )
                    }
                    rc_statistics_buy_product.layoutManager = GridLayoutManager(this,3)
                    rc_statistics_buy_product.setHasFixedSize(true)
                    val adapter = ProductsHomeAdapter(productData)
                    rc_statistics_buy_product.adapter = adapter
                }

            }



    }



}

