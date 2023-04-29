package com.example.project_1.activities.restaurant.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.project_1.R
import com.example.project_1.activities.MainActivity
import com.example.project_1.fragments.client.ClientCategoriesFragment
import com.example.project_1.fragments.client.ClientOrdersFragment
import com.example.project_1.fragments.client.ClientProfileFragment
import com.example.project_1.fragments.restaurant.RestaurantCategoryFragment
import com.example.project_1.fragments.restaurant.RestaurantOrdersFragment
import com.example.project_1.fragments.restaurant.RestaurantProductFragment
import com.example.project_1.models.User
import com.example.project_1.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class RestaurantHomeActivity : AppCompatActivity() {

    private val TAG = "CLIENT_HOME_ACTIVITY"

    lateinit var sharedPref : SharedPref

    lateinit var bottomNavigationBar : BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_home)
        sharedPref = SharedPref(this)

        getUserSession()
        this.bottomNavigationBar = findViewById(R.id.bottom_navigation)
        openFragment(RestaurantOrdersFragment())
        bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    openFragment(RestaurantOrdersFragment())
                    true
                }
                R.id.item_category -> {
                    openFragment(RestaurantCategoryFragment())
                    true
                }
                R.id.item_product -> {
                    openFragment(RestaurantProductFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }
        //findViewById<Button>(R.id.buttonLogout).setOnClickListener{logout()}
    }

    private fun openFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getUserSession() {
        val gson = Gson()
        if (!this.sharedPref.getData("user").isNullOrBlank()) {
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            Log.i(TAG, "getUserSession: $user")

        }
    }
}