package com.example.project_1.activities.client.shopping_bag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.activities.client.address.create.ClientAddressCreateActivity
import com.example.project_1.activities.client.address.list.ClientAddressListActivity
import com.example.project_1.adapters.ShoppingBagAdapter
import com.example.project_1.models.Product
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientShoppingBagActivity : AppCompatActivity() {

    private val TAG = "CLIENT_SHOPPING_BAG_ACTIVITY"

    var recyclerView : RecyclerView ? = null
    var textTotal : TextView ? = null
    var buttonContinue : Button ? = null
    var toolbar : Toolbar? = null

    var adapter : ShoppingBagAdapter ? = null
    var sharedPref : SharedPref? = null
    var gson = Gson()

    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shopping_bag)
        sharedPref = SharedPref(this)

        recyclerView = findViewById(R.id.recyclerView)
        textTotal = findViewById(R.id.textBalance)
        buttonContinue = findViewById(R.id.buttonContinue)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Tu orden"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        recyclerView?.layoutManager = LinearLayoutManager(this)
        setSupportActionBar(toolbar)
        getProductsFromSharedPref()
        buttonContinue?.setOnClickListener { goToAddressList() }
    }

    private fun goToAddressList() {
        val intent = Intent(this, ClientAddressListActivity::class.java)
        startActivity(intent)
    }

    fun setTotal(total : Double) {
        textTotal?.text = "${total}$"
    }


    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
            adapter = ShoppingBagAdapter(this, selectedProducts)
            recyclerView?.adapter = adapter
        }
    }
}