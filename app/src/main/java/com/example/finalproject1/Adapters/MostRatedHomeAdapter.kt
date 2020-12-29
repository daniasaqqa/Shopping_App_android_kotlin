package com.example.finalproject1.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject1.Activities.ProductDetails
import com.example.finalproject1.Models.MostRateHomeModel
import com.example.finalproject1.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_product_recycle.view.*
import kotlinx.android.synthetic.main.most_rated_home.view.*

class MostRatedHomeAdapter ( var data: MutableList<MostRateHomeModel>) :
    RecyclerView.Adapter<MostRatedHomeAdapter.MyViewHolder>() {
    lateinit var con: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_category=itemView.img_most_rated_home
        var tv_cat_ad=itemView.name_prod_rate
        var tv_price= itemView.price_rate
        var tv_decrip= itemView.descrp_rate
        var card_rc_prod_home=itemView.card_rate_home
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
    ): MostRatedHomeAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.most_rated_home, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        con=holder.card_rc_prod_home.context

        if (data[position].product_img_model!!.isNotEmpty()){
        Picasso.get().load(data[position].product_img_model).into(holder.img_category)
    }else{
       // Picasso.get().load(data[position].product_img_model).into(R.drawable.img_slide_tow as ImageView)
            holder.img_category.setImageResource(R.drawable.img_slide_tow)
    }
        // holder.img_category.setImageURI(Uri.parse(data[position].product_img_model))
        holder.tv_cat_ad.text = data[position].product_text_model
        holder.tv_price.text =data[position].most_price
        holder.tv_decrip.text =data[position].most_descrip

        holder.card_rc_prod_home.setOnClickListener {

            var intent = Intent (con , ProductDetails::class.java)
            intent.putExtra("productId",data[position].id)
            con.startActivity(intent)

        }

        //holder.bind(data[position],itemClickListener)
    }

//    interface  OnItemClickListener{
//        fun onItemClick(user:ProductHomeModel)
//    }

}
