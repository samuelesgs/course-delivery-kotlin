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
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

class RestaurantCategoryFragment : Fragment() {

    private val TAG = "RESTAURANT_CATEGORY_FRAGMENT"
    var myView : View? = null
    var imageViewCategory : ImageView ? = null
    var editCategoryName : EditText ? = null
    var imageFile : File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

    private fun createCategory() {
        val category = editCategoryName?.text.toString()
        if (imageFile != null) {

        } else {
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
        }
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