package com.example.finalproject1.Activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalproject1.Adapters.ProductsHomeAdapter
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_category_categories.*

class CategoryCategories : AppCompatActivity() {

    var fb = FirebaseFirestore.getInstance()

    var cid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_categories)

        // get category id from category adapter
        cid = intent.getStringExtra("catName")

        // function get name category
        headName()




        //function get product data
        productData()


        //intet to main 2 activity
        btn_back_category_categories.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
            finish()
        }
    }

    private fun headName() {


        // Gat category Id
        var cid = intent.getStringExtra("catId")!!

        // get  document id to category
        var ref = fb.collection("category").document(cid)

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {

                    //Get Array of my Add Category
                    var data = documentSnapshot.data
                    val nameCat = data!!["categorytName"] as String
                    category_name_category_categories.text = nameCat


                } else {
                    Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }


    }

    private fun productData() {
        // Products in category
        //  val cId:String=intent.getStringExtra("catName")!!
        val dataCat = mutableListOf<ProductHomeModel>()
        fb.collection("product").whereEqualTo("productType", cid)
            .get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result!!) {
                        val id = document.id
                        val data = document.data
                        val productName = data["productName"] as String?
                        val productImage = data["productImage"] as String?
                        dataCat.add(
                            ProductHomeModel(
                                id,
                                productImage,
                                productName

                            )
                        )
                    }
                    //add adapter to my recycle
                    // number col & layout type grid recycle
                    rc_categories_category.layoutManager = GridLayoutManager(this, 3)
                    rc_categories_category.setHasFixedSize(true)
                    val Adapter = ProductsHomeAdapter(dataCat)
                    rc_categories_category.adapter = Adapter
                }

            }
    }
}
