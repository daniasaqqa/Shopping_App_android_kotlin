package com.example.finalproject1.Activities

import android.content.Context
import android.content.Intent
import android.media.Rating
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.example.finalproject1.Models.ProductHomeModel
//import com.bumptech.glide.Glide
import com.example.finalproject1.Models.Users
import com.example.finalproject1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetails : AppCompatActivity() {

    var email: String? = ""
    var adminAccount = false
    var c = 0f
    var pId = ""
    lateinit var oldRate: RatingBar
    lateinit var rateSysArr: ArrayList<String>
    lateinit var rate: java.util.ArrayList<String>
    var rateSysF = false
    val fuser = FirebaseAuth.getInstance().currentUser
    var productbuyCounter: Double = 0.0

    var img=""
    var nameProd = ""
    var datashow= mutableListOf<ProductHomeModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
      // user = FirebaseAuth.getInstance().currentUser!!
        pId = intent.getStringExtra("productId")!!
//        pId = intent.getStringExtra("prodd")!!

        productBuyAddArray(fuser!!.uid)

        val fireUser = FirebaseAuth.getInstance().currentUser
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var data = documentSnapshot.data
                     nameProd = data!!["productName"] as String
                    val priceProd = data["productPrice"] as String
                    val phoneProd = data["productPhone"] as String
                    val emailProd = data["productEmail"] as String
                    val descripProd = data["productDescription"] as String
                     img = data["productImage"] as String
                    val type = data["productType"] as String
                    val ra = data["productRating"] as Double
                    productbuyCounter = data["productbuyCounter"] as Double
                    rateSysArr = data!!["rateSystem"] as ArrayList<String>
                    rate = data!!["rateConutUser"] as ArrayList<String>
                    product_name_product_details.text = nameProd
                    price_product_details.text = priceProd
                    description_product_details.text = descripProd
                    phone_product_details_category.text = phoneProd
                    email_product_details_category.text = emailProd

                   // imageView3.setImageURI(Uri.parse(img))
                    if (img.isNotEmpty()) {
                        Picasso.get().load(img).into(imageView3)
                    }else{
                      //  Picasso.get().load(img).into(R.drawable.img_slide_tow as ImageView)
                        imageView3.setImageResource(R.drawable.img_slide_tow)
                    }
                    type_product_details_category.text = type
                    rate_bar_product_details.rating = ra.toFloat()
                   // oldRate=RatingBar(this)
                    oldRate = rate_bar_product_details
                    Log.e("dna", nameProd)
                } else {
                    Toast.makeText(this, "Failer", Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }


        // pop up Menu
        val popup = PopupMenu(this, btn_menu_product_details)
        popup.menuInflater.inflate(R.menu.product, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit_menu -> {
                    // intent Edit  this product
                    val intent = Intent(this, EditProduct::class.java)
                    intent.putExtra("prodId", pId)
                    startActivity(intent)
                    Toast.makeText(this, "Edit btn", Toast.LENGTH_LONG).show()
                }
                R.id.delete_menu -> {
                    //delete this product
                    fb.collection("product").document(pId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Deleted Success", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, Main2Activity::class.java))
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Deleted Not Success", Toast.LENGTH_LONG).show()
                        }

                }
            }
            true
        }
        //menu show click the btn
        btn_menu_product_details.setOnClickListener {
            popup.show()
        }

        btn_back_product_details.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
            finish()
        }
        btn_buy.setOnClickListener {
            productBuy(pId,img,nameProd)
            datashow.add(ProductHomeModel(pId,img,nameProd))
            btn_buy.visibility = View.GONE
            btn_buy_animate.visibility = View.VISIBLE
            updateproductbuyCounter()


        }

        //show & hide data

        if (fireUser != null) {
            email = fireUser.email
        }

        if (email.equals("admin@gmail.com")) {
            adminAccount = true
            val shPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val editor = shPref.edit()
            editor.putBoolean("Admin Account", adminAccount)

            val b = editor.commit()
            if (b)
                Toast.makeText(this, "Added Success ", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "Added Field", Toast.LENGTH_LONG).show()
        }

        if (adminAccount) {
            btn_menu_product_details.visibility = View.VISIBLE
        } else {
            btn_menu_product_details.visibility = View.GONE
        }

        btn_location_map_product.setOnClickListener {
            var i = Intent(this, MapUser::class.java)
            i.putExtra("prodlocId", pId)
            startActivity(i)

        }




        rateSysFun(fuser!!.uid)
        rate_product_details_category.setOnClickListener {

            var ratingr = rate_bar_product_details.rating

            if (rateSysF == false) {

                if (ratingr >= 1) {
                   var ratingr = rate_bar_product_details.rating.toString()
                    //  rateSysArr = ArrayList<String>()
                    rateSysArr.add(fuser!!.uid)
                    updateuserArray(rateSysArr)
                    // rate = ArrayList<String>()
                    rate.add(ratingr)
                    updateRatingArray(rate)
                    rateSysF=true
                }

            }

        }

    }

    private fun updateproductbuyCounter() {

        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)

        var ProductbuyCounter = productbuyCounter + 1.0
        ref.update("productbuyCounter", ProductbuyCounter)
            .addOnSuccessListener {
                Toast.makeText(this, "Success Edit", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
            }

    }


    fun rateSysFun(userId: String) {

        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var data = documentSnapshot.data
                    rateSysArr = data!!["rateSystem"] as ArrayList<String>
                    rate = data!!["rateConutUser"] as ArrayList<String>

                    for (product in rateSysArr) {
                        if (product == userId) {
                            rateSysF = true
                        }
                    }
                    var count = 0f
                    for (product in rate) {
                        count += product.toString().toFloat()
                    }
                    c = count / rate.size
                    updateRating(c)

                    Log.e("dna", c.toString())
                } else {
                    Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStop() {
        super.onStop()


    }

    fun updateRating(r: Float) {
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)
        ref.update("productRating", r)
            .addOnSuccessListener {
                Toast.makeText(this, "Success Edit", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
            }
    }

    fun updateuserArray(rateSys: ArrayList<String>) {
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)
        ref.update("rateSystem", rateSys)
            .addOnSuccessListener {
                Toast.makeText(this, "Success user Edit", Toast.LENGTH_LONG).show()
               // startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
            }
    }

    fun updateRatingArray(Ratings_array: ArrayList<String>) {
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("product").document(pId)
        ref.update("rateConutUser", Ratings_array)
            .addOnSuccessListener {
                Toast.makeText(this, "Success rate Edit", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
            }
    }


    fun productBuy(pId: String , img: String , nameProd: String){
        datashow.add(ProductHomeModel(pId,img,nameProd))
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(fuser!!.uid)
        ref.update("productBuy",datashow)
            .addOnSuccessListener {
               // Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()

            }
    }

    fun productBuyAddArray(id :String){
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null){
                    var data = documentSnapshot.data
                    datashow= data!!["productBuy"] as MutableList<ProductHomeModel>
                }else{
                    Toast.makeText(this,"feild Edit",Toast.LENGTH_LONG).show()
                }

            }
    }

}


