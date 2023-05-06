package com.example.project_1.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.adapters.ProductAdapter
import com.example.project_1.models.Product
import com.example.project_1.models.User
import com.example.project_1.providers.ProductsProvider
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {


    private val TAG ="CLIENT_PRODUCTS_LIST_ACTIVITY"
    var recyclerView: RecyclerView? = null
    var sharedPref : SharedPref? = null
    var adapter : ProductAdapter ? = null
    var user : User? = null
    var productsProvider : ProductsProvider ? = null
    var products : ArrayList<Product> = ArrayList()

    var idCategory : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)
        idCategory = intent.getStringExtra("idCategory")
        sharedPref = SharedPref(this)
        getUser()
        productsProvider = ProductsProvider(user?.sessionToken)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = GridLayoutManager(this, 2)
        getProducts()
    }

    private fun getUser() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun getProducts() {
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object : Callback<ArrayList<Product>> {
            override fun onResponse( call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>> ) {
                Log.i(TAG, "onResponse: ${response}")
                if (response.body() != null) {
                    products = response.body()!!
                    adapter = ProductAdapter(this@ClientProductsListActivity, products)
                    recyclerView?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Log.e(TAG, "onFailure: $t" )
                Toast.makeText(this@ClientProductsListActivity, "ERROR : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}