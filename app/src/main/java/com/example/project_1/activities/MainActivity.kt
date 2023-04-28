package com.example.project_1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.project_1.R
import com.example.project_1.activities.client.home.ClientHomeActivity
import com.example.project_1.activities.delivery.home.DeliveryHomeActivity
import com.example.project_1.activities.restaurant.home.RestaurantHomeActivity
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.UsersProvider
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var sharedPref: SharedPref
    lateinit var editEmail : EditText
    lateinit var editPassword : EditText
    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        sharedPref = SharedPref(this)
        getUserSession()
        findViewById<Button>(R.id.buttonRegister).setOnClickListener{
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonLogin).setOnClickListener{ login() }
    }

    private fun login() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
         usersProvider.login(email, password)?.enqueue(object  : Callback<ResponseHttp> {
             override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                 Log.i("MAiN_ACTIVITY", "onResponse: ${response.body()}")
                 if (response.body()?.isSuccess == true) {
                     Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                     saveUserSession(response.body()?.data.toString())
                 } else {
                     Toast.makeText(this@MainActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                 }
             }

             override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                 Log.i("MAiN_ACTIVITY", "Hubo un error ${t.message}")
                 Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
             }
         });
    }

    private fun saveUserSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
        if (user.roles?.size!! > 1) {
            goToSelectRole()
        } else {
            goToClientHome()
        }
    }

    private fun goToClientHome() {
        val intent = Intent(this, ClientHomeActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun goToSelectRole() {
        val intent = Intent(this, SelectRolesActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun getUserSession() {
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            if (!sharedPref.getData("rol").isNullOrBlank()) {
                val rol = sharedPref.getData("rol").toString().replace("\"","")
                Log.i("MAINACTIVITY", "getUserSession: "+ rol)
                when (rol) {
                    "CLIENTE" -> {
                        intent = Intent(this, ClientHomeActivity::class.java)
                    }
                    "RESTAURANTE" -> {
                        intent = Intent(this, RestaurantHomeActivity::class.java)
                    }
                    "REPARTIDOR" -> {
                        intent = Intent(this, DeliveryHomeActivity::class.java)
                    }
                }
                if (intent != null) {
                    finish()
                    startActivity(intent)
                }
            } else  {
                //goToClientHome()
            }
        }
    }
}