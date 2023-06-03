package com.example.project_1.activities.delivery.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.activities.delivery.orders.map.DeliveryOrdersMapActivity
import com.example.project_1.activities.restaurant.home.RestaurantHomeActivity
import com.example.project_1.adapters.OrderProductsAdapter
import com.example.project_1.models.Order
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.OrderProvider
import com.example.project_1.providers.UsersProvider
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersDetailActivity : AppCompatActivity() {

    val TAG = "DELIVERY_ORDERS_DETAIL_ACTIVITY"

    var order: Order? = null
    val gson = Gson()
    var sharedPref : SharedPref? = null

    var textClient : TextView? = null
    var textAddress : TextView? = null
    var textDate : TextView? = null
    var textStatus : TextView? = null
    var textBalance : TextView? = null
    var recyclerView : RecyclerView? = null


    var usersProvider: UsersProvider ? = null
    var orderProvider: OrderProvider ? = null

    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_detail)
        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)
        sharedPref = SharedPref(this)
        val user = sharedPref?.getUserFromSession()
        usersProvider = UsersProvider(user?.sessionToken)
        orderProvider = OrderProvider(user?.sessionToken!!)

        textClient = findViewById(R.id.textClient)
        textAddress = findViewById(R.id.textAddress)
        textDate = findViewById(R.id.textDate)
        textStatus = findViewById(R.id.textStatus)
        textBalance = findViewById(R.id.textBalance)
        recyclerView = findViewById(R.id.recyclerView)

        textClient?.text = "${order?.client?.name} ${order?.client?.lastname}"
        textAddress?.text = order?.address?.address
        textDate?.text = "${order?.timestamp}"
        textStatus?.text = order?.status

        recyclerView?.layoutManager = LinearLayoutManager(this)

        val buttonTrack = findViewById<Button>(R.id.buttonTrack)
        val buttonMap = findViewById<Button>(R.id.buttonMap)


        if (order?.status.equals("DESPACHADO")) {
            buttonTrack?.visibility = View.VISIBLE
        } else if (order?.status.equals("EN CAMINO")){
            buttonMap?.visibility = View.VISIBLE
        }

        buttonTrack.setOnClickListener {
            update()
        }
        buttonMap.setOnClickListener { goToMap() }

        adapter()

        getTotal()
    }

    private fun goToMap() {
        val intent = Intent(this, DeliveryOrdersMapActivity::class.java)
        startActivity(intent)
    }

    private fun update() {
        Log.i(TAG, "update: ")
        orderProvider?.updateToOnTheWay(order!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.i(TAG, "onResponse: $response")
                if (response.body() != null) {
                    if (response.body()?.isSuccess == true) {
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "Entrega iniciada", Toast.LENGTH_SHORT).show()
                        goToMap()
                    } else {
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "No se pudo iniciar la entrega", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DeliveryOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DeliveryOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        });
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