package com.example.project_1.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project_1.R
import com.example.project_1.activities.client.home.ClientHomeActivity
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.UsersProvider
import com.example.project_1.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    private val TAG = "SAVE_IMAGE_ACTIVITY"

    private lateinit var circleImageView : CircleImageView

    private var imageFile : File? = null
    private var userProviders : UsersProvider ? = null
    private var sharedPref : SharedPref? = null
    private var user : User ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)
        sharedPref = SharedPref(this)
        getUserSession()
        userProviders = UsersProvider(user?.sessionToken)
        circleImageView = findViewById(R.id.imageUser)
        circleImageView.setOnClickListener { selectImage() }
        findViewById<Button>(R.id.buttonSkip).setOnClickListener {  }
        findViewById<Button>(R.id.buttonConfirm).setOnClickListener { saveImage() }
    }

    private fun saveImage() {
        if (imageFile != null && user != null) {
            userProviders?.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.i(TAG, "onResponse: $response")
                    saveUserSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@SaveImageActivity, "ERROR : ${t.message}", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "onFailure: ${t.message} " )
                }

            })
        } else {
            Toast.makeText(this, "GRACIOSO PERO NO GRACIOSO DE BUENO SI NO DE RARO", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
        goToClientHome()
    }

    private fun goToClientHome() {
        val intent = Intent(this, ClientHomeActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun getUserSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result : ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = fileUri?.path?.let { File(it) }
                circleImageView.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.e("SAVE_IMAGE_ACTIVITY", "ImagePicker.getError(data) : " + ImagePicker.getError(data) )
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent {
                    intent -> startImageForResult.launch(intent)
            }
    }
}