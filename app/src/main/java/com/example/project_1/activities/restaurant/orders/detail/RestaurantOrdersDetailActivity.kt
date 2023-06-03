package com.example.project_1.activities.restaurant.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
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

class RestaurantOrdersDetailActivity : AppCompatActivity() {

    private val TAG = "CLIENT_ORDERS_DETAIL_ACTIVITY"

    var order: Order? = null
    val gson = Gson()
    var sharedPref : SharedPref? = null
    var toolbar : Toolbar? = null

    var textClient : TextView? = null
    var textAddress : TextView? = null
    var textDate : TextView? = null
    var textStatus : TextView? = null
    var textBalance : TextView? = null
    var textDeliveryAvailable : TextView? = null
    var textDeliveryAssigned : TextView? = null
    var recyclerView : RecyclerView? = null
    var spinnerDeliveryMen : Spinner ? = null

    var usersProvider: UsersProvider ? = null
    var orderProvider: OrderProvider ? = null

    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_orders_detail)
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
        textDeliveryAvailable = findViewById(R.id.textDeliveryAvailable)
        textDeliveryAssigned = findViewById(R.id.textDeliveryAssigned)
        recyclerView = findViewById(R.id.recyclerView)
        spinnerDeliveryMen = findViewById(R.id.spinnerDeliveryMen)

        textClient?.text = "${order?.client?.name} ${order?.client?.lastname}"
        textAddress?.text = order?.address?.address
        textDate?.text = "${order?.timestamp}"
        textStatus?.text = order?.status
        val textDelivery = findViewById<TextView>(R.id.textDelivery)
        val textDelivery1 = findViewById<TextView>(R.id.textDelivery1)
        val textDelivery2 = findViewById<TextView>(R.id.textDelivery2)
        //textDeliveryAvailable?.text = "${order?.delivery?.name} ${order?.delivery?.lastname}"
        textDeliveryAssigned?.text = "${order?.delivery?.name} ${order?.delivery?.lastname}"

        recyclerView?.layoutManager = LinearLayoutManager(this)

        val buttonTrack = findViewById<Button>(R.id.buttonTrack)

        Log.i(TAG, "onCreate: ${order?.status}")
        if (order?.status == "PAGADO") {
            Log.i(TAG, "TRUE: ")
            buttonTrack?.visibility = View.VISIBLE
            textDelivery?.visibility = View.VISIBLE
            textDelivery2?.visibility = View.VISIBLE
            textDeliveryAvailable?.visibility = View.VISIBLE
            spinnerDeliveryMen?.visibility = View.VISIBLE
        }
        if(!order?.status.equals("PAGADO")) {
            Log.i(TAG, "!order?.status.equals(PAGADO)")
            textDelivery1?.visibility = View.VISIBLE
            textDeliveryAssigned?.visibility = View.VISIBLE
            textDeliveryAssigned?.visibility = View.VISIBLE
        }

        buttonTrack.setOnClickListener {
            update()
        }

        adapter()

        getTotal()
        getDeliveryMen()
    }

    private fun goToOrders() {
        val intent = Intent(this, RestaurantHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun update() {
        order?.idDelivery = idDelivery
        orderProvider?.updateToDispatched(order!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null) {
                    if (response.body()?.isSuccess == true) {
                        Toast.makeText(this@RestaurantOrdersDetailActivity, "Repartidor asignado correctamente", Toast.LENGTH_SHORT).show()
                        goToOrders()
                    } else {
                        
                    }
                } else {
                    Toast.makeText(this@RestaurantOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@RestaurantOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        });
    }

    private fun getDeliveryMen() {
        Log.i(TAG, "getDeliveryMen: ")
        usersProvider?.getFindDeliveryMen()?.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                Log.i(TAG, "onResponse: $response")
                if (response.body() != null) {
                    val deliveryMen = response.body()
                    Log.i(TAG, "onResponse: $deliveryMen")

                    val arrayAdapter = ArrayAdapter(this@RestaurantOrdersDetailActivity, android.R.layout.simple_dropdown_item_1line, deliveryMen!!)
                    spinnerDeliveryMen?.adapter = arrayAdapter
                    spinnerDeliveryMen?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long ) {
                            idDelivery = deliveryMen[position].id!!
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@RestaurantOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
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