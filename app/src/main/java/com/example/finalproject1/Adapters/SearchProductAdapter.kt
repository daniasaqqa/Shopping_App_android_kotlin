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
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject1.Activities.ProductDetails
import com.example.finalproject1.Fragments.SearchFragment
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_recycle_style.view.*

class SearchProductAdapter ( var data: MutableList<ProductHomeModel>) :
    RecyclerView.Adapter<SearchProductAdapter.MyViewHolder>() {
    lateinit var con: Context
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_search=itemView.img_search
        var tv_search=itemView.tv_search
        var card_search=itemView.card_search
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
    ): SearchProductAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_recycle_style, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  holder.img_search.setImageURI(Uri.parse(data[position].product_img_model))

        if (data[position].product_img_model!!.isNotEmpty()){
        Picasso.get().load(data[position].product_img_model).into(holder.img_search)
    }else{
       // Picasso.get().load(data[position].product_img_model).into(R.drawable.img_slide_tow as ImageView)
            holder.img_search.setImageResource(R.drawable.img_slide_tow)
    }
        holder.tv_search.text = data[position].product_text_model

        con=holder.card_search.context
        holder.card_search.setOnClickListener {

            var intent = Intent (con , ProductDetails::class.java)
            intent.putExtra("productId",data[position].id)
            var bundle=Bundle()
            bundle.putString("prodName",data[position].product_text_model)
            var f=SearchFragment()
            f.arguments=bundle
            Log.d("dna" ,data[position].id)
            con.startActivity(intent)
        }

        //holder.bind(data[position],itemClickListener)
    }

//    interface  OnItemClickListener{
//        fun onItemClick(user:ProductHomeModel)
//    }

}
