package com.example.project_1.fragments.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.adapters.CategoriesAdapter
import com.example.project_1.models.Category
import com.example.project_1.models.User
import com.example.project_1.providers.CategoriesProvider

class ClientCategoriesFragment : Fragment() {

    var myView : View? = null
    var recyclerView : RecyclerView ? = null
    var categoriesProvider : CategoriesProvider ? = null
    var adapter : CategoriesAdapter ? = null
    var user : User? = null
    var categories = ArrayList<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)

        //session
        //instance provider
        //findbyViewId
        //peticion

        return myView
    }
}