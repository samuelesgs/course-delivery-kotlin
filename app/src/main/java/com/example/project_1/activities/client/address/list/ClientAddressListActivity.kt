package com.example.project_1.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project_1.R
import com.example.project_1.activities.client.address.create.ClientAddressCreateActivity
import com.example.project_1.adapters.AddressAdapter
import com.example.project_1.models.Address
import com.example.project_1.models.User
import com.example.project_1.providers.AddressProvider
import com.example.project_1.utils.SharedPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        this.getAddress()

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