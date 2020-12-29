package com.example.finalproject1.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.example.finalproject1.Activities.*
import com.example.finalproject1.Models.Users

import com.example.finalproject1.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_statistics_most_rated.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var email: String? = ""
    var adminAccount = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)


        val fireUser = FirebaseAuth.getInstance().currentUser
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(fireUser!!.uid)



        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var data = documentSnapshot.data
                    val username = data!!["username"] as String
                    val email = data["userEmail"] as String
                    val phone = data["userMobile"] as String
                    val aboutme = data["aboutMe"] as String
                    val img = data["userImage"] as String

                    root.username_profile.text = username
                    root.phone_profile.text = phone
                    root.email_profilee.text = email
                    root.about_me_profile.text = aboutme
                   // root.profile_img.setImageURI(Uri.parse(img))
                    if (img.isNotEmpty()){
                    Picasso.get().load(img).into(root.profile_img)}
                    else{
                        root.profile_img.setImageResource(R.drawable.userimg)
                    }
                } else {
                    Toast.makeText(activity!!, "Failer", Toast.LENGTH_LONG).show()
                }
            }

        //show & hide data
        if (fireUser != null) {
            email = fireUser.email
        }

        if (email.equals("admin@gmail.com")) {
            adminAccount = true
            val shPref = activity!!.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val editor = shPref.edit()
            editor.putBoolean("Admin Account", adminAccount)

            val b = editor.commit()
            if (b)
                Toast.makeText(activity!!, "Added Success ", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(activity!!, "Added Field", Toast.LENGTH_LONG).show()
        }

        if (adminAccount) {
            root.imf_btn_categories.visibility = View.VISIBLE
            root.img_btn_statistics_most.visibility = View.VISIBLE
            root.img_btn_statistics_buy.visibility = View.VISIBLE
            root.categories_profile.visibility = View.VISIBLE
            root.statistics_buy_profile.visibility = View.VISIBLE
            root.statistics_most_profile.visibility = View.VISIBLE
            root.img_btn_pruched.visibility = View.GONE
            root.user_pruched_profile.visibility = View.GONE
        } else {
            root.img_btn_pruched.visibility = View.VISIBLE
            root.user_pruched_profile.visibility = View.VISIBLE

            root.imf_btn_categories.visibility = View.GONE
            root.img_btn_statistics_most.visibility = View.GONE
            root.img_btn_statistics_buy.visibility = View.GONE
            root.categories_profile.visibility = View.GONE
            root.statistics_buy_profile.visibility = View.GONE
            root.statistics_most_profile.visibility = View.GONE
        }



        root.edit_profile.setOnClickListener {
            startActivity(Intent(activity!!, EditProfile::class.java))
        }
        root.imf_btn_categories.setOnClickListener {
            startActivity(Intent(activity!!, CategoryProfile::class.java))
        }

        root.categories_profile.setOnClickListener {
            startActivity(Intent(activity!!, CategoryProfile::class.java))
        }

        root.user_pruched_profile.setOnClickListener {
            startActivity(Intent(activity!!,UserPruched::class.java))
        }

        root.img_btn_pruched.setOnClickListener {
            startActivity(Intent(activity!!,UserPruched::class.java))
        }
        root.statistics_buy_profile.setOnClickListener {
            startActivity(Intent(activity!!,StatisticsBuyProduct::class.java))
        }

        root.img_btn_statistics_buy.setOnClickListener {
            startActivity(Intent(activity!!,StatisticsBuyProduct::class.java))
        }

        root.statistics_most_profile.setOnClickListener {
            startActivity(Intent(activity!!,StatisticsMostRated::class.java))
        }

        root.btn_change_password_profile.setOnClickListener {
            startActivity(Intent(activity!!,ChangePassword::class.java))
        }


        root.delete_account.setOnClickListener {

            var dialog= AlertDialog.Builder(activity!!)
            dialog.setTitle("Delete Account")
            dialog.setMessage("you want delete Account?")
            dialog.setIcon(R.drawable.delete_forever_icon)
            dialog.setCancelable(false)
            dialog.setPositiveButton("Yes"){ dialogInterface,i->

                val user= FirebaseAuth.getInstance().currentUser!!


                var fb = FirebaseFirestore.getInstance()
                var ref = fb.collection("users").document(user!!.uid)
                ref.delete()
                    .addOnCompleteListener {it->
                        if (it.isSuccessful){
                            startActivity(Intent(activity!!,Login::class.java))
                            activity!!.finish()
                            Toast.makeText(activity!!,"Deleted account is successfully",Toast.LENGTH_SHORT).show()
                        }

                    }
               user.delete()
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful){
                           Toast.makeText(activity!!,"Deleted account is successfully",Toast.LENGTH_SHORT).show()
             }
                   }

            }
            dialog.setNegativeButton("No"){_,_->


            }
            dialog.show()
        }

        root.logout_profile.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity!!, Login::class.java))
        }

        return root
    }}

