package com.example.project_1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.adapters.RolesAdapter
import com.example.project_1.models.User
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson

class SelectRolesActivity : AppCompatActivity() {

    lateinit var recyclerView  : RecyclerView
    var user : User? = null
    lateinit var adapter : RolesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getUserFromSession()
        adapter = RolesAdapter(this, user?.roles!!)
        recyclerView.adapter = adapter
    }

    private fun getUserFromSession() {

        val sharedPref  = SharedPref(this)
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}