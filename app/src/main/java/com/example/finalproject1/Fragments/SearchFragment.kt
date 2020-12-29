package com.example.finalproject1.Fragments

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject1.Adapters.CategoryAdapter
import com.example.finalproject1.Adapters.CategorySearchAdapter
import com.example.finalproject1.Adapters.SearchProductAdapter
import com.example.finalproject1.Models.CategoryModel
import com.example.finalproject1.Models.ProductHomeModel

import com.example.finalproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.rc_search_layout


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    var fb = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root=inflater.inflate(R.layout.fragment_search, container, false)

       root.rc_search_layout.visibility=View.GONE
        readData()
        categoryAllData()




     root.editText.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                productData(newText)
                categoryData(newText)
                root.rc_search_layout.visibility=View.VISIBLE
               return true
            }

        })






//        root.imageButton2.setOnClickListener {
//

//           productData(root.editText.toString())
//        }


        return root
    }



    private fun productData(name:String){
        val productData= mutableListOf<ProductHomeModel>()
        fb.collection("product").whereEqualTo("SearchProductName",name)
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
                    rc_search_layout.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    rc_search_layout.setHasFixedSize(true)
                    val adapter = SearchProductAdapter(productData)
                    rc_search_layout.adapter = adapter
                }

            }



    }

    private fun readData(){
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
                    rc_search_layout.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    rc_search_layout.setHasFixedSize(true)
                    val Adapter = SearchProductAdapter(productData)
                    rc_search_layout.adapter = Adapter
                }

            }

    }

    private fun categoryData(name:String){

        val catData= mutableListOf<CategoryModel>()
        fb.collection("category").whereEqualTo("nameSearch",name)
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
                    rc_search_layout.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    rc_search_layout.setHasFixedSize(true)
                    val Adapter = CategorySearchAdapter(catData)
                    rc_search_layout.adapter = Adapter
                }
            }
    }

    private fun categoryAllData(){
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
                    rc_search_layout.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    rc_search_layout.setHasFixedSize(true)
                    val adapter = CategorySearchAdapter(catData)
                    rc_search_layout.adapter = adapter
                }
            }

    }


}

