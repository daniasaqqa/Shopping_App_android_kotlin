package com.example.finalproject1.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.finalproject1.Activities.ProductDetails
import com.example.finalproject1.Adapters.CategoryAdapter
import com.example.finalproject1.Adapters.MostRatedHomeAdapter
import com.example.finalproject1.Adapters.ProductsHomeAdapter
import com.example.finalproject1.Adapters.SlideAdapter
import com.example.finalproject1.Models.CategoryModel
import com.example.finalproject1.Models.MostRateHomeModel
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
//    lateinit var topAnimation: Animation
//    lateinit var middleAnimation: Animation
    var fb = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)



        //slider

        val productData= mutableListOf<ProductHomeModel>()
        fb.collection("product").limit(3)
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

                    var slide = root.slider_home_vp
                    root.indicator.setViewPager(slide)
                    val adapter = SlideAdapter(activity!!,productData)
                    slide!!.adapter = adapter
                }

            }
       // productSlideData()


        //Catigoties





        productData()
        categoryData()
        mostRatedproductData()
        return root
    }

    private fun productData(){
        val productData= mutableListOf<ProductHomeModel>()
        fb.collection("product")
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
                    product_rec.layoutManager = GridLayoutManager(activity!!, 3)
                    category_recycler.setHasFixedSize(true)
                    val Adapter = ProductsHomeAdapter(productData)
                    product_rec.adapter = Adapter
                }

            }
    }


    private fun categoryData(){
        val catData= mutableListOf<CategoryModel>()
        fb.collection("category")
            .get()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        val id=document.id
                        val data=document.data

                        val productName = data["categorytName"] as String?
                        val productImage = data["categoryImage"] as String?
                        catData.add(
                            CategoryModel(id,
                                productImage,
                                productName

                            )
                        )
                    }
                    category_recycler.layoutManager =
                        LinearLayoutManager(activity!!, RecyclerView.HORIZONTAL, false)
                    product_rec.setHasFixedSize(true)

                    val Adapter = CategoryAdapter(catData)
                    category_recycler.adapter = Adapter

                    }
                }

            }

    private fun mostRatedproductData(){
        val productData= mutableListOf<MostRateHomeModel>()
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
                        val priceProd = data["productPrice"] as String
                        val descripProd = data["productDescription"] as String


                        productData.add(
                            MostRateHomeModel(
                                id,
                                productImage,
                                productname,
                                descripProd,
                                priceProd

                            )
                        )
                    }
                    most_rate_home_recycler.layoutManager =
                        LinearLayoutManager(activity!!, RecyclerView.HORIZONTAL, false)

                    most_rate_home_recycler.setHasFixedSize(true)
                    val adapter = MostRatedHomeAdapter(productData)
                    most_rate_home_recycler.adapter = adapter
                }

            }



    }


}




