package com.example.project_1.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project_1.R
import com.example.project_1.activities.client.address.create.ClientAddressCreateActivity
import com.example.project_1.activities.client.payments.form.ClientPaymentFormActivity
import com.example.project_1.adapters.AddressAdapter
import com.example.project_1.adapters.ShoppingBagAdapter
import com.example.project_1.models.*
import com.example.project_1.providers.AddressProvider
import com.example.project_1.providers.OrderProvider
import com.example.project_1.utils.SharedPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    val TAG = "CLIENT_ADDRESS_LIST_ACTIVITY"

    var fabCreate : FloatingActionButton? = null
    var toolbar : Toolbar ? = null

    var recyclerView : RecyclerView ? = null
    var adapterAddress : AddressAdapter ? = null

    var sharedPref : SharedPref ? = null
    var user : User? = null

    var address = ArrayList<Address>()

    var addressProvider : AddressProvider ? = null
    var ordersProvider : OrderProvider ? = null

    var selectedProducts = ArrayList<Product>()

    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)
        user = sharedPref?.getUserFromSession()

        fabCreate = findViewById(R.id.fabAdd)
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView?.layoutManager = LinearLayoutManager(this)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.purple_200))
        toolbar?.title = "Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fabCreate?.setOnClickListener { goToAddressCreate() }

        addressProvider = AddressProvider(user?.sessionToken!!)
        ordersProvider = OrderProvider(user?.sessionToken!!)
        this.getAddress()
        this.getProductsFromSharedPref()
        findViewById<Button>(R.id.buttonContinue).setOnClickListener { getAddressFromSession() }

    }

    private fun createOrder(idAddress : String) {
        val order = Order(
            products = selectedProducts,
            idClient = user?.id!!,
            idAddress = idAddress
        );

        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null) {
                    Toast.makeText(this@ClientAddressListActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ClientAddressListActivity, "Ocurrio un error al crear la orden", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
            }

        });
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
        }
    }

    private fun getAddressFromSession() {
        if (!sharedPref?.getData("address").isNullOrBlank()) {
            val model = gson.fromJson(sharedPref?.getData("address"), Address::class.java)
            createOrder(model.id!!)
            //goToPaymentsForm()
        } else {
            Toast.makeText(this, "Selecciona una direccion para continuar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToPaymentsForm() {
        val intent = Intent(this, ClientPaymentFormActivity::class.java)
        startActivity(intent)
    }

    fun resetValue(position : Int) {
        val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
        val view = viewHolder?.itemView
        val imageViewCheck = view?.findViewById<ImageView>(R.id.imageView)
        imageViewCheck?.visibility = View.GONE
    }

    private fun getAddress() {
        addressProvider?.getAddress(user?.id!!)?.enqueue(object: Callback<ArrayList<Address>>{
            override fun onResponse(call: Call<ArrayList<Address>>,response: Response<ArrayList<Address>>) {

                if (response.body() != null) {
                    address = response.body()!!
                    adapterAddress = AddressAdapter(this@ClientAddressListActivity, address)
                    recyclerView?.adapter = adapterAddress
                } else {
                    Toast.makeText(this@ClientAddressListActivity, "Fallo request ${response.message()}", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goToAddressCreate() {
        val intent = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(intent)
    }
}