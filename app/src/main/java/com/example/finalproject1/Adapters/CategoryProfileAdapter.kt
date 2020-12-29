package com.example.finalproject1.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject1.Activities.CategoryCategories
import com.example.finalproject1.Activities.EditCategory
import com.example.finalproject1.Models.CategoryModel
import com.example.finalproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycle_category_profile.*
import kotlinx.android.synthetic.main.recycle_category_profile.view.*
import kotlinx.android.synthetic.main.recycle_category_profile.view.menu_category_profile

class CategoryProfileAdapter (var data: MutableList<CategoryModel>) :
        RecyclerView.Adapter<CategoryProfileAdapter.MyViewHolder>() {

    var con: Context?=null
    lateinit var conte:Context

    var fb = FirebaseFirestore.getInstance()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_category=itemView.img_category_profile
        var tv_cat_ad=itemView.tv_category_profile
        var btn_menu=itemView.menu_category_profile
        var card_cat_prof=itemView.card_cat_prof




    }
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CategoryProfileAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_category_profile, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  holder.img_category.setImageURI(Uri.parse(data[position].category_img_model))
        if (data[position].category_img_model!!.isNotEmpty()){
        Picasso.get().load(data[position].category_img_model).into(holder.img_category)
    }else{
      //  Picasso.get().load(data[position].category_img_model).into(R.drawable.img_slide_tow as ImageView)
            holder.img_category.setImageResource(R.drawable.img_slide_tow)
    }

        holder.tv_cat_ad.text = data[position].category_text_model
        holder.btn_menu.setOnClickListener(View.OnClickListener  {
            //menu category
            val popup= PopupMenu(conte,holder.btn_menu)
            popup.inflate(R.menu.product)
            popup.setOnMenuItemClickListener {item->
                when(item.itemId){
                    R.id.edit_menu-> {
                        var i= Intent(conte, EditCategory::class.java)
                        i.putExtra("catyId",data[position].id)
                        conte.startActivity(i)
                        Toast.makeText(conte,"Edit btn",Toast.LENGTH_LONG).show()}
                    R.id.delete_menu->{



                        var dialog= AlertDialog.Builder(conte)
                        dialog.setTitle("Delete")
                        dialog.setMessage("you want delete this Category?")
                        dialog.setIcon(R.drawable.delete_forever_icon)
                        dialog.setCancelable(false)
                        dialog.setPositiveButton("Yes"){ dialogInterface,i->
                            fb.collection("category").document(data[position].id).delete()
                                .addOnSuccessListener {

                                    Toast.makeText(conte,"Deleted Success",Toast.LENGTH_LONG).show()

                                }
                                .addOnFailureListener {
                                    Toast.makeText(conte,"Deleted Not Success",Toast.LENGTH_LONG).show()
                                }

                        }
                        dialog.setNegativeButton("No"){_,_->


                        }
                        dialog.show()


                        Toast.makeText(conte,"Delete",Toast.LENGTH_LONG).show()}
                }

                popup.show()
                true

            }
            popup.show()
        })
        conte=holder.card_cat_prof.context
        holder.card_cat_prof.setOnClickListener {

            var intent = Intent (conte , CategoryCategories::class.java)
            intent.putExtra("catId",data[position].id)
            Log.d("dna" ,data[position].id)
            conte.startActivity(intent)
        }

    }
}