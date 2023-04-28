package com.example.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.project_1.R
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.UsersProvider
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {


    val TAG = "REGISTER_ACTIVITY"

    var usersProvider = UsersProvider()

    lateinit var editName : EditText
    lateinit var editLastName : EditText
    lateinit var editEmail : EditText
    lateinit var editPhone : EditText
    lateinit var editPassword : EditText
    lateinit var editConfirmPassword : EditText
    lateinit var imageClose : ImageView
    lateinit var buttonRegister : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editName = findViewById(R.id.editName)
        editLastName = findViewById(R.id.editLastName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        editPassword = findViewById(R.id.editPassword)
        editConfirmPassword = findViewById(R.id.editConfirmPassword)
        imageClose = findViewById(R.id.imageLogout)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            register()
        }
        imageClose.setOnClickListener{finish()}
    }

    private fun register() {
        val  name : String = editName.text.toString()
        val  lastName : String = editLastName.text.toString()
        val  email : String = editEmail.text.toString()
        val  phone : String = editPhone.text.toString()
        val  password : String = editPassword.text.toString()
        val  confirmPassword : String = editConfirmPassword.text.toString()

        if (password == confirmPassword){
            val user = User(
                name = name,
                lastname = lastName,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    //Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    saveUserSession(response.body()?.data.toString())
                    goToSaveImage()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.i(TAG, "Error : ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Falle" , Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun saveUserSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
        this.goToSaveImage()
    }

    private fun goToSaveImage() {
        val intent = Intent(this, SaveImageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        finish()
        startActivity(intent)
    }
}