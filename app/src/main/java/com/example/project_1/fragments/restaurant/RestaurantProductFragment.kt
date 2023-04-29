package com.example.project_1.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project_1.R
import com.example.project_1.adapters.CategoriesAdapter
import com.example.project_1.models.Category
import com.example.project_1.models.User
import com.example.project_1.providers.CategoriesProvider
import com.example.project_1.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantProductFragment : Fragment() {

    private val TAG = "RESTAURANT_PRODUCT_FRAGMENT"

    private var editName : EditText ? = null
    private var editDescription : EditText ? = null
    private var editTextPrice : EditText ? = null
    private var image1 : ImageView ? = null
    private var image2 : ImageView ? = null
    private var image3 : ImageView ? = null
    private var spinnerCategories : Spinner ? = null

    private var imageFile1 : File ? = null
    private var imageFile2 : File ? = null
    private var imageFile3 : File ? = null

    private lateinit var sharedPref : SharedPref
    private var categoriesProvider : CategoriesProvider? = null
    private var user : User? = null
    private var categories = ArrayList<Category>()
    private var idCategory = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = SharedPref(requireActivity())
        getUserSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        loadCategories()
        val myView = inflater.inflate(R.layout.fragment_restaurant_product, container, false)
        editName = myView?.findViewById(R.id.editName)
        editDescription = myView?.findViewById(R.id.editDescription)
        editTextPrice = myView?.findViewById(R.id.editPrice)
        image1 = myView?.findViewById(R.id.image1)
        image2 = myView?.findViewById(R.id.image2)
        image3 = myView?.findViewById(R.id.image3)
        spinnerCategories = myView?.findViewById(R.id.spinnerCategories)
        myView?.findViewById<Button>(R.id.buttonSave)?.setOnClickListener {
            saveProduct()
        }

        image1?.setOnClickListener { selectImage(101) }
        image2?.setOnClickListener { selectImage(102) }
        image3?.setOnClickListener { selectImage(103) }

        return myView
    }

    private fun saveProduct() {
        val name = editName?.text.toString()
        val description = editDescription?.text.toString()
        val priceText = editTextPrice?.text.toString()
        if (isValidForm(name, description, priceText)) {
            
        }
    }

    private fun isValidForm(name : String, description : String, price: String) : Boolean{
        if (name.isBlank()){
            Toast.makeText(requireContext(), "Ingresa el nombre del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (description.isBlank()){
            Toast.makeText(requireContext(), "Ingresa descripcion del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (price.isBlank()){
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageFile1 == null) {
            Toast.makeText(requireContext(), "Selecciona la imagen 1", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile2 == null) {
            Toast.makeText(requireContext(), "Selecciona la imagen 2", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile3 == null) {
            Toast.makeText(requireContext(), "Selecciona la imagen 3", Toast.LENGTH_SHORT).show()
            return false
        }
        if (idCategory.isBlank()) {
            Toast.makeText(requireContext(), "Selecciona la categoria del product", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loadCategories() {
        categoriesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>) {
                Log.i(TAG, "onResponse: "+response)
                if (response.body() != null) {
                    categories = response.body()!!
                    val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, categories)
                    spinnerCategories?.adapter = arrayAdapter
                    spinnerCategories?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( adapterView: AdapterView<*>?, view: View?, position: Int, l: Long ) {
                            idCategory = categories[position].id!!
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                    }
                    /*adapter = CategoriesAdapter(requireContext(), categories)
                    recyclerView?.adapter = adapter*/
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                when (requestCode) {
                    101 -> {
                        imageFile1 = File(fileUri?.path.toString())
                        image1?.setImageURI(fileUri)
                    }
                    102 -> {
                        imageFile2 = File(fileUri?.path.toString())
                        image2?.setImageURI(fileUri)
                    }
                    103 -> {
                        imageFile3 = File(fileUri?.path.toString())
                        image3?.setImageURI(fileUri)
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .start(requestCode)
    }

}