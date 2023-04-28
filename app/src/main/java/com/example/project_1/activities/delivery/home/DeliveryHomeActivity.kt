package com.example.project_1.activities.delivery.home

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
import com.example.project_1.fragments.delivery.DeliveryOrdersFragment
import com.example.project_1.models.User
import com.example.project_1.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class DeliveryHomeActivity : AppCompatActivity() {
    private val TAG = "CLIENT_HOME_ACTIVITY"

    lateinit var sharedPref : SharedPref

    lateinit var bottomNavigationBar : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)
        sharedPref = SharedPref(this)

        getUserSession()
        this.bottomNavigationBar = findViewById(R.id.bottom_navigation)
        openFragment(DeliveryOrdersFragment())
        bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_orders -> {
                    openFragment(DeliveryOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout() {
        this.sharedPref.remove("user")
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun getUserSession() {
        val gson = Gson()
        if (!this.sharedPref.getData("user").isNullOrBlank()) {
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            Log.i(TAG, "getUserSession: $user")

        }
    }
}