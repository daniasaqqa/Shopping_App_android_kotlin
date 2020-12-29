package com.example.finalproject1.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.finalproject1.Fragments.AddFragment
import com.example.finalproject1.Fragments.HomeFragment
import com.example.finalproject1.Fragments.ProfileFragment
import com.example.finalproject1.Fragments.SearchFragment
import com.example.finalproject1.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_home.*

class Main2Activity : AppCompatActivity() {
 lateinit  var fragmentMan: FragmentManager
    var email: String? = ""
    var adminAccount=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val firebaseUser= FirebaseAuth.getInstance().currentUser
        if (firebaseUser !=null){
            // Name ,email,address
            email=firebaseUser.email
        }

        if (email.equals("admin@gmail.com")){
            adminAccount=true
            val shPref=getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val editor=shPref.edit()
            editor.putBoolean("Admin Account",adminAccount)

            val b=editor.commit()
            if (b)
                Toast.makeText(this,"Added Success ",Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this,"Added Field",Toast.LENGTH_LONG).show()
        }

        supportFragmentManager.beginTransaction().replace(
            R.id.mainContainer,
            HomeFragment()
        ).commit()

        if(adminAccount){
            menu.setMenuResource(R.menu.nav_bottom)
        }else{
            menu.setMenuResource(R.menu.nav_bottom_user)
        }
       menu.setOnItemSelectedListener { it->
           var fragment:Fragment?=null
                when (it) {
                   R.id.home_navv ->
                     fragment=HomeFragment()
                    R.id.search_navv->
                        fragment=SearchFragment()
                    R.id.add_navv ->
                        fragment=AddFragment()
                    R.id.profile_navv ->
                       fragment=ProfileFragment()


                }
                if (fragment !=null){
                   fragmentMan= supportFragmentManager
                    fragmentMan.beginTransaction().replace(
                        R.id.mainContainer,
                      fragment
                    ).commit()
                }else{
                   Toast.makeText(this@Main2Activity,"sorry",Toast.LENGTH_LONG).show()
                }
                true
            }


    }
}
