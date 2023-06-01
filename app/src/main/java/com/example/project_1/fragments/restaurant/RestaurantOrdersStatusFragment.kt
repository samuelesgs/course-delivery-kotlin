package com.example.project_1.fragments.restaurant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.adapters.OrdersClientAdapter
import com.example.project_1.adapters.OrdersRestaurantAdapter
import com.example.project_1.models.Order
import com.example.project_1.models.User
import com.example.project_1.providers.OrderProvider
import com.example.project_1.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantOrdersStatusFragment : Fragment() {

    val TAG = "RESTAURANT_ORDERS_STATUS_FRAGMENT"
    var status: String = ""
    var myView: View? = null
    var ordersProvider: OrderProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null

    var recyclerView: RecyclerView? = null
    var adapter: OrdersRestaurantAdapter? = null


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_restaurant_orders_status, container, false)
        status = arguments?.getString("status")!!

        sharedPref = SharedPref(requireActivity())
        user = sharedPref?.getUserFromSession()
        ordersProvider = OrderProvider(user?.sessionToken!!)

        recyclerView = myView?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        getOrders()

        return myView
    }

    private fun getOrders() {
        Log.i(TAG, "getOrders: $status")
        ordersProvider?.getOrdersByStatus(status)?.enqueue(object:
            Callback<ArrayList<Order>> {
            override fun onResponse(call: Call<ArrayList<Order>>, response: Response<ArrayList<Order>>) {
                Log.i("RESPONSE_ORDERS", "onResponse: ${response.body()}")
                if (response.body() != null) {
                    val orders = response.body()
                    adapter = OrdersRestaurantAdapter(requireActivity(), orders!!)
                    recyclerView?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}