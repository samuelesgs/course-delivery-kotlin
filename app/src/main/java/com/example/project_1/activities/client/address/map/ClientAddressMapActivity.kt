package com.example.project_1.activities.client.address.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.project_1.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class ClientAddressMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "CLIENT_ADDRESS_MAP_ACTIVITY"

    var googleMap : GoogleMap? = null

    val PERMISSION_ID = 42
    var fusedLocationClient : FusedLocationProviderClient? = null

    var textViewAddress : TextView ? = null

    var city = ""
    var country = ""
    var address = ""
    var addressLatLng : LatLng ? = null


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation = locationResult.lastLocation
            Log.d(TAG, "callback: $lastLocation")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        textViewAddress = findViewById(R.id.textviewAddress)

        getLastLocation()
        findViewById<Button>(R.id.buttonRegister).setOnClickListener { goToCreateAddress() }
    }

    private fun goToCreateAddress() {
        val intent = Intent()
        intent.putExtra("city",city)
        intent.putExtra("address", address)
        intent.putExtra("country", country)
        intent.putExtra("lat", addressLatLng?.latitude)
        intent.putExtra("lng", addressLatLng?.longitude)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onCameraMove() {
        googleMap?.setOnCameraIdleListener {
            try {
                val geocoder = Geocoder(this)
                addressLatLng = googleMap?.cameraPosition?.target
                val addressList = geocoder.getFromLocation(addressLatLng?.latitude!!, addressLatLng?.longitude!!, 1)
                if (addressList != null) {
                    city = addressList[0].locality
                    country = addressList[0].countryName
                    address = addressList[0].getAddressLine(0)
                    textViewAddress?.text = "$address $city"
                }
            } catch (e : Exception) {
                Log.d(TAG, "ERROR: ${e.message}")
            }
        }
    }

    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply{
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun isLocationEnable() : Boolean {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (checkPermission()) {

            if (isLocationEnable()) {
                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder().target(
                                LatLng(location.latitude, location.longitude)
                            ).zoom(15f).build()
                        ))
                    }
                }
            } else {
                Toast.makeText(this, "Habilita la localizacion", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }


        } else {
            requestPermissions()
        }
    }

    private fun checkPermission() : Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        onCameraMove()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }
}