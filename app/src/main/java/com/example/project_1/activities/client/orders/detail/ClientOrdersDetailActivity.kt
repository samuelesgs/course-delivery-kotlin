package com.example.project_1.activities.client.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.activities.client.orders.map.ClientOrdersMapActivity
import com.example.project_1.adapters.OrderProductsAdapter
import com.example.project_1.models.Order
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson

class ClientOrdersDetailActivity : AppCompatActivity() {

    private val TAG = "CLIENT_ORDERS_DETAIL_ACTIVITY"
    
    var order:Order? = null
    val gson = Gson()
    var sharedPref : SharedPref ? = null
    var toolbar : Toolbar? = null

    var textClient : TextView? = null
    var textAddress : TextView? = null
    var textDate : TextView? = null
    var textStatus : TextView? = null
    var textBalance : TextView? = null
    var recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_detail)
        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)
        Log.i(TAG, "onCreate: ${order?.toJson()}")

        toolbar = findViewById(R.id.toolbar)
        textClient = findViewById(R.id.textClient)
        textAddress = findViewById(R.id.textAddress)
        textDate = findViewById(R.id.textDate)
        textStatus = findViewById(R.id.textStatus)
        textBalance = findViewById(R.id.textBalance)
        recyclerView = findViewById(R.id.recyclerView)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Order #${order?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textClient?.text = "${order?.client?.name} ${order?.client?.lastname}"
        textAddress?.text = order?.address?.address
        textDate?.text = "${order?.timestamp}"
        textStatus?.text = order?.status

        recyclerView?.layoutManager = LinearLayoutManager(this)

        adapter()

        getTotal()
        val buttonTrack = findViewById<Button>(R.id.buttonTrack)

        if (order?.status == "EN CAMINO") {
            buttonTrack.visibility = View.VISIBLE
        }

        buttonTrack.setOnClickListener { goToMap() }
    }

    private fun goToMap() {
        val intent = Intent(this, ClientOrdersMapActivity::class.java)
        intent.putExtra("order", order?.toJson())
        startActivity(intent)
    }

    private fun adapter() {
        val products = order?.products
        val adapter = OrderProductsAdapter(this, products!!)
        recyclerView?.adapter = adapter
    }

    private fun getTotal() {
        var total = 0.0

        for (p in order?.products!!) {
            total += (p.price * p.quantity)
        }
        textBalance?.text =  "$total $"
    }
}