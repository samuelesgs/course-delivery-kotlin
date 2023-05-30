package com.example.project_1.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.project_1.R
import com.example.project_1.activities.client.address.create.ClientAddressCreateActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientAddressListActivity : AppCompatActivity() {


    var fabCreate : FloatingActionButton? = null
    var toolbar : Toolbar ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)
        fabCreate = findViewById(R.id.fabAdd)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.purple_200))
        toolbar?.title = "Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fabCreate?.setOnClickListener { goToAddressCreate() }
    }

    private fun goToAddressCreate() {
        val intent = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(intent)
    }
}