package com.example.finalproject1.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.example.finalproject1.Activities.ProductDetails
import com.example.finalproject1.Models.CategoryModel
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_product_recycle.view.*

class ProductsHomeAdapter ( var data: MutableList<ProductHomeModel>) :
    RecyclerView.Adapter<ProductsHomeAdapter.MyViewHolder>() {
      lateinit var con:Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_category=itemView.img_product_home
        var tv_cat_ad=itemView.name_product_home
        var card_rc_prod_home=itemView.card_rc_prod_home
//        fun bind(user:ProductHomeModel,clickListener: OnItemClickListener){
//            img_category.setImageURI(Uri.parse(user.product_img_model))
//          tv_cat_ad.text = user.product_text_model
//            lateinit var con:Context
//            card_rc_prod_home.setOnClickListener {
//                var intent=Intent(con,ProductDetails::class.java)
//
//                intent.putExtra("productId",user.id)
//
//                Log.e("dna",user.id)
//                        con.startActivity(intent)
//            }
//            itemView.setOnClickListener {
//               clickListener.onItemClick(user)
//           }
//        }


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsHomeAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_product_recycle, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        con=holder.card_rc_prod_home.context

        if (data[position].product_img_model!!.isNotEmpty()) {
            Picasso.get().load(data[position].product_img_model).into(holder.img_category)
            // holder.img_category.setImageURI(Uri.parse(data[position].product_img_model))
        } else{
       // Picasso.get().load(data[position].product_img_model).into(R.drawable.img_slide_tow as ImageView)
            holder.img_category.setImageResource(R.drawable.img_slide_tow)
    }






        holder.tv_cat_ad.text = data[position].product_text_model


        holder.card_rc_prod_home.setOnClickListener {

            var intent = Intent (con , ProductDetails::class.java)
            intent.putExtra("productId",data[position].id)
            Log.d("dna" ,data[position].id)
            con.startActivity(intent)
        }

        //holder.bind(data[position],itemClickListener)
    }

//    interface  OnItemClickListener{
//        fun onItemClick(user:ProductHomeModel)
//    }

}
