package com.example.project_1.fragments.restaurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project_1.R
import com.example.project_1.models.Category
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.CategoriesProvider
import com.example.project_1.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantCategoryFragment : Fragment() {

    private val TAG = "RESTAURANT_CATEGORY_FRAGMENT"
    private var myView : View? = null
    private var imageViewCategory : ImageView ? = null
    private var editCategoryName : EditText ? = null
    private var imageFile : File? = null

    private var categoriesProvider : CategoriesProvider? = null
    private var sharedPref : SharedPref? = null
    private var user : User ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPref = SharedPref(requireActivity())
        this.getUserSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken)
        myView =  inflater.inflate(R.layout.fragment_restaurant_category, container, false)
        editCategoryName = myView?.findViewById(R.id.editCategoryName)
        imageViewCategory = myView?.findViewById(R.id.imageViewCategory)

        myView?.findViewById<Button>(R.id.buttonSave)?.setOnClickListener {
            createCategory()
        }
        imageViewCategory?.setOnClickListener {
            selectImage()
        }

        return myView
    }

    private fun getUserSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun createCategory() {
        val categoryName = editCategoryName?.text.toString()
        if (imageFile != null) {
            val category = Category(name = categoryName)
            categoriesProvider?.create(imageFile!!, category)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.i(TAG, "onResponse: $response")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()
                    if (response.body()?.isSuccess == true) {
                        clearForms()
                    }
                    //saveUserSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(requireContext(), "ERROR : ${t.message}", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "onFailure: ${t.message} " )
                }

            })
        } else {
            Toast.makeText(requireContext(), "GRACIOSO PERO NO GRACIOSO DE BUENO SI NO DE RARO", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearForms() {
        editCategoryName?.setText("")
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result : ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = fileUri?.path?.let { File(it) }
            imageViewCategory?.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Log.e("SAVE_IMAGE_ACTIVITY", "ImagePicker.getError(data) : " + ImagePicker.getError(data) )
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
    }

    private fun selectImage() {
        Log.i(TAG, "selectImage: ")
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent {
                    intent -> startImageForResult.launch(intent)
            }
    }

}