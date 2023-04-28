package com.example.project_1.activities.client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.project_1.R
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

class ClientUpdateActivity : AppCompatActivity() {

    private val TAG = "CLIENT_UPDATE_ACTIVITY"

    var circleImageUser : CircleImageView? = null
    var editTextName :  EditText? = null
    var editTextLastname : EditText? = null
    var editTextPhone : EditText? = null
    var buttonUpdate : Button? = null

    var sharedPref : SharedPref? = null
    var user : User? = null

    private var imageFile : File? = null
    var userProviders : UsersProvider ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)

        sharedPref = SharedPref(this)

        circleImageUser = findViewById(R.id.circleImageView)
        editTextName = findViewById(R.id.editName)
        editTextLastname = findViewById(R.id.editLastName)
        editTextPhone = findViewById(R.id.editPhone)
        buttonUpdate = findViewById(R.id.btn_update)

        getUserFromSession()
        userProviders = UsersProvider(user?.sessionToken)

        editTextName?.setText(user?.name)
        editTextLastname?.setText(user?.lastname)
        editTextPhone?.setText(user?.phone)

        if (!user?.image.isNullOrBlank()) {
            Glide.with(this).load(user?.image).into(circleImageUser!!)
        }

        circleImageUser?.setOnClickListener {selectImage()}
        buttonUpdate?.setOnClickListener { updateData() }

    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun saveUserSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
        //goToClientHome()
    }

    private fun updateData() {

        val name = editTextName?.text.toString()
        val lastname = editTextLastname?.text.toString()
        val phone = editTextPhone?.text.toString()
        user?.name = name
        user?.lastname = lastname
        user?.phone = phone
        if (imageFile?.exists() == true){
            userProviders?.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.i(TAG, "onResponse: $response")
                    saveUserSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientUpdateActivity, "ERROR : ${t.message}", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "onFailure: ${t.message} " )
                }

            })
        } else {
            userProviders?.updateWithoutImage(user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.isSuccess == true) {
                        saveUserSession(response.body()?.data.toString())

                    } else {

                    }
                    Log.i(TAG, "onResponse: $response")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientUpdateActivity, "ERROR : ${t.message}", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "onFailure: ${t.message} " )
                }

            })
        }


    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result : ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = fileUri?.path?.let { File(it) }
            circleImageUser?.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Log.e("SAVE_IMAGE_ACTIVITY", "ImagePicker.getError(data) : " + ImagePicker.getError(data) )
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
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