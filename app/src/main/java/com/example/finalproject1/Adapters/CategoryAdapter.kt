package com.example.finalproject1.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject1.Activities.CategoryCategories
import com.example.finalproject1.Models.CategoryModel
import com.example.finalproject1.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.categories_recycle.view.*

class CategoryAdapter( var data: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    lateinit var con: Context
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

      var img_category=itemView.category_rc_img
      var tv_cat_ad=itemView.tv_category_rc
       var con_cat=itemView.con_cat


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.categories_recycle, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CategoryAdapter.MyViewHolder, position: Int) {
      //  holder.img_category.setImageURI(Uri.parse(data[position].category_img_model))
        if (data[position].category_img_model!!.isNotEmpty()){
        Picasso.get().load(data[position].category_img_model).into(holder.img_category)
    }else{
       // Picasso.get().load(data[position].category_img_model).into(R.drawable.img_slide_tow as ImageView)
            holder.img_category.setImageResource(R.drawable.img_slide_tow)
    }

        holder.tv_cat_ad.text = data[position].category_text_model

        con=holder.con_cat.context
        holder.con_cat.setOnClickListener {

            var intent = Intent (con , CategoryCategories::class.java)
            intent.putExtra("catName",data[position].category_text_model)
            intent.putExtra("catId",data[position].id)
            Log.d("dna" ,data[position].category_text_model)
            con.startActivity(intent)
        }
    }
}