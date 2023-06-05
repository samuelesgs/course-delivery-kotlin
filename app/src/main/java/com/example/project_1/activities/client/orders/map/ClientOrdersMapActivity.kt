package com.example.project_1.activities.client.orders.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.project_1.R
import com.example.project_1.activities.delivery.home.DeliveryHomeActivity
import com.example.project_1.models.Order
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.providers.OrderProvider
import com.example.project_1.utils.SharedPref
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.maps.route.extensions.drawRouteOnMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientOrdersMapActivity : AppCompatActivity(), OnMapReadyCallback {


    private var deliveryLocation: LatLng ? = null
    private val TAG = "DELIVERY_ORDER_MAP_ACTIVITY"

    var marketDelivery : Marker? = null
    var marketAddress : Marker? = null

    var googleMap : GoogleMap? = null

    val PERMISSION_ID = 42
    var fusedLocationClient : FusedLocationProviderClient? = null


    var city = ""
    var country = ""
    var address = ""
    var addressLatLng : LatLng? = null
    var myLocationLatLng : LatLng? = null

    var textNeighborhood : TextView? = null
    var textAddress : TextView? = null
    var imageDelivery : ImageView? = null
    var textDeliveryName : TextView? = null

    var order : Order? = null

    var gson = Gson()

    val REQUEST_PHONE_CALL = 30

    var ordersProvider : OrderProvider? = null

    var user : User?  = null
    var sharedPref: SharedPref? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            myLocationLatLng = LatLng(lastLocation!!.latitude, lastLocation.longitude)
            googleMap?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(
                        LatLng(myLocationLatLng?.latitude!!, myLocationLatLng?.longitude!!)
                    ).zoom(15f).build()
                ))
            removeDeliveryMarker()
            addDeliveryMarker()

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_map)
        order = gson.fromJson(intent.getStringExtra("order"),Order::class.java)
        if (order?.lat != null && order?.lng != null) {
            deliveryLocation = LatLng(order?.lat!!, order?.lng!!)
        }
        sharedPref = SharedPref(this)
        user = sharedPref?.getUserFromSession()
        ordersProvider = OrderProvider(user?.sessionToken!!)
        addressLatLng = LatLng(order?.address?.lat!!, order?.address?.lng!!)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        textNeighborhood = findViewById(R.id.textNeighborhood)
        textAddress = findViewById(R.id.textAddress)
        imageDelivery = findViewById(R.id.imageDelivery)
        textDeliveryName = findViewById(R.id.textDeliveryName)

        setData()

        getLastLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fusedLocationClient != null) {
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        }
    }

  private fun setData() {
        val address = order?.address?.address
        val neighborhood = order?.address?.neighborhood
        val clientName = "${order?.delivery?.name} ${order?.delivery?.lastname}"
        if (!order?.delivery?.image.isNullOrBlank()) {
            Glide.with(this).load(order?.delivery?.image).into(imageDelivery!!)
        }
        textAddress?.text = address
        textNeighborhood?.text = neighborhood
        textDeliveryName?.text = clientName
        findViewById<ImageView>(R.id.imagePhone).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            } else {
                call()
            }
        }
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${order?.delivery?.phone}")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permiso denegado para realizar la llamada", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(intent)

    }

    private fun removeDeliveryMarker() {
        marketDelivery?.remove()
    }

    private fun drawRoute() {
        if (deliveryLocation != null) {
            val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)
            googleMap?.drawRouteOnMap(getString(R.string.google_map_api_key), source = deliveryLocation!!, destination = addressLocation, context = this, color = Color.GREEN, polygonWidth = 10, boundMarkers = false, markers = false)
        }
    }

    private fun addDeliveryMarker(){
        if (deliveryLocation != null) {
            marketDelivery = googleMap?.addMarker(
                MarkerOptions()
                    .position(deliveryLocation!!)
                    .title("Posicion repartidor").icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery)))
        }
    }

    private fun addAddressMarker(){
        val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)
        Log.i(TAG, "addAddressMarker: ${order?.toJson()}")
        marketAddress = googleMap?.addMarker(
            MarkerOptions()
                .position(addressLocation)
                .title("Mi Direccion").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_location))
        )
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
                    if (location != null){
                        myLocationLatLng = LatLng(location.latitude, location.longitude)

                        removeDeliveryMarker()
                        addDeliveryMarker()
                        addAddressMarker()
                        drawRoute()
                        if (deliveryLocation != null) {
                            googleMap?.moveCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.builder().target(
                                        LatLng(deliveryLocation?.latitude!!, deliveryLocation?.longitude!!)
                                    ).zoom(15f).build()
                                ))
                        }

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
        googleMap?.uiSettings?.isZoomControlsEnabled = true
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

        if (requestCode == REQUEST_PHONE_CALL) {
            call()
        }
    }
}