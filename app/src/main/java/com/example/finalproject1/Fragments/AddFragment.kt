package com.example.finalproject1.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject1.Activities.AddCategory
import com.example.finalproject1.Activities.AddProduct

import com.example.finalproject1.R
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

/**
 * A simple [Fragment] subclass.
 */
class AddFragment : Fragment() {

  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    var root= inflater.inflate(R.layout.fragment_add, container, false)
    root.btn_add_product.setOnClickListener {
      startActivity(Intent(activity!!,AddProduct::class.java))
    }

    root.btn_add_category.setOnClickListener {
      startActivity(Intent(activity!!,AddCategory::class.java))
    }
    return root
  }

}