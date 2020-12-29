package com.example.finalproject1.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
//import com.bumptech.glide.Glide
import com.example.finalproject1.Activities.ProductDetails
import com.example.finalproject1.Models.ProductHomeModel
import com.example.finalproject1.R
import com.squareup.picasso.Picasso

class SlideAdapter(private val context:Context,var images:MutableList<ProductHomeModel>):PagerAdapter() {
    private var layoutInflater:LayoutInflater?=null
    lateinit var con:Context

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view===`object`
    }

    override fun getCount(): Int {
       return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
       layoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v=layoutInflater!!.inflate(R.layout.slider_image_home,null)
        val image=v.findViewById<View>(R.id.image_slide) as ImageView
        val card_rc_slide_prod=v.findViewById<View>(R.id.card_rc_slide_prod)
        con=card_rc_slide_prod.context
        card_rc_slide_prod.setOnClickListener {
            var intent = Intent (con , ProductDetails::class.java)
            intent.putExtra("productId",images[position].id)
            Log.d("dna" ,images[position].id)
            con.startActivity(intent)
        }
        //Glide.with(con).load(images[position].product_img_model).into(image)

        if (images[position].product_img_model!!.isNotEmpty()){
        Picasso.get().load(images[position].product_img_model).into(image)
    }else{
      //  Picasso.get().load(images[position].product_img_model).into(R.drawable.img_slide_tow as ImageView)
            image.setImageResource(R.drawable.img_slide_tow)
    }
    //    image.setImageURI(Uri.parse(images[position].product_img_model))
        val vp=container as ViewPager
        vp.addView(v,0)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp=container as ViewPager
        val v=`object` as View
        vp.removeView(v)
    }
}