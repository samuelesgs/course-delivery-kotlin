package com.example.project_1.fragments.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.adapters.CategoriesAdapter
import com.example.project_1.models.Category
import com.example.project_1.models.User
import com.example.project_1.providers.CategoriesProvider
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientCategoriesFragment : Fragment() {

    private val TAG = "CLIENT_CATEGORIES_FRAGMENT"

    private var myView : View? = null
    private var recyclerView : RecyclerView ? = null

    private var categoriesProvider : CategoriesProvider ? = null
    private var adapter : CategoriesAdapter ? = null
    private var user : User? = null
    private var categories = ArrayList<Category>()

    private lateinit var sharedPref : SharedPref
    private var toolbar : Toolbar ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)
        sharedPref = SharedPref(requireActivity())

        //session
        getUserSession()
        //instance provider

        categoriesProvider = CategoriesProvider(user?.sessionToken)
        //findbyViewId
        toolbar = myView?.findViewById(R.id.toolbar)
        recyclerView = myView?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        toolbar?.title = "Categorias"
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        //peticion
        loadCategories()

        return myView
    }

    private fun loadCategories() {
        categoriesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse( call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>> ) {
                Log.i(TAG, "onResponse: "+response)
                if (response.body() != null) {
                    categories = response.body()!!
                    adapter = CategoriesAdapter(requireContext(), categories)
                    recyclerView?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun getUserSession() {
        val gson = Gson()
        if (!this.sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}