package com.example.project_1.activities.client.address.create

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.project_1.R
import com.example.project_1.activities.client.address.list.ClientAddressListActivity
import com.example.project_1.activities.client.address.map.ClientAddressMapActivity
import com.example.project_1.models.Address
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.AddressProvider
import com.example.project_1.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressCreateActivity : AppCompatActivity() {


    var toolbar : Toolbar ? = null
    var editTextRefPoint : EditText? = null

    var addressLat = 0.0
    var addressLng = 0.0

    var editLocation : EditText ? = null
    var editDirection : EditText ? = null

    var addressProvider : AddressProvider ? = null
    var sharedPref : SharedPref ? = null
    var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_create)

        sharedPref = SharedPref(this)
        user = sharedPref?.getUserFromSession()
        addressProvider = AddressProvider(user?.sessionToken!!)

        editTextRefPoint = findViewById(R.id.editPointReferences)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Nueva direccion"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        editLocation = findViewById(R.id.editLocation)
        editDirection = findViewById(R.id.editDirection)

        editTextRefPoint?.setOnClickListener {goToAddressMap()}
        findViewById<Button>(R.id.buttonCreate).setOnClickListener {
            createAddress()
        }
    }

    private fun createAddress() {
        val direction = editDirection?.text.toString()
        val location = editLocation?.text.toString()
        if (isValidForm(direction, location)) {
            val addressModel = Address(
                address = direction,
                neighborhood =  location,
                id_user =  user?.id,
                lat = addressLat,
                lng = addressLng
            )
            Log.i("CLIENT_CREATE", "createAddress: ${addressModel.toJson()}")
            Log.i("CLIENT_CREATE", "TOKEN: ${user?.sessionToken!!}")
            addressProvider?.create(addressModel)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.i("CLIENT_CREATE", "onResponse: $response")
                    if (response.body() != null) {
                        Toast.makeText(this@ClientAddressCreateActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goToAddressList()
                    } else {
                        Toast.makeText(this@ClientAddressCreateActivity, "Ocurrio un error en la peticion", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientAddressCreateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun goToAddressList() {
        val intent = Intent(this, ClientAddressListActivity::class.java)
        startActivity(intent)
    }

    private fun isValidForm(direction: String, location : String) : Boolean{
        if (direction.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa la direccion", Toast.LENGTH_SHORT).show()
            return false
        }
        if (location.isNullOrBlank()) {
            Toast.makeText(this, "Ingrese el barrio o recidencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLat == 0.0) {
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLng == 0.0) {
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val city = data?.getStringExtra("city")
            val address = data?.getStringExtra("address")
            val country = data?.getStringExtra("country")
            addressLat = data?.getDoubleExtra("lat", 0.0)!!
            addressLng = data?.getDoubleExtra("lng", 0.0)!!
            editTextRefPoint?.setText("$address $city")
        }
    }

    private fun goToAddressMap() {
        val intent = Intent(this, ClientAddressMapActivity::class.java)
        resultLauncher.launch(intent)
    }
}