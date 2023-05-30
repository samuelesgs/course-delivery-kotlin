package com.example.project_1.providers

import com.example.project_1.api.ApiRoutes
import com.example.project_1.models.Address
import com.example.project_1.models.ResponseHttp
import com.example.project_1.routes.AddressRoutes
import retrofit2.Call

class AddressProvider(val token: String) {

    private var addressRoutes : AddressRoutes ? = null

    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }

    fun getAddress(idUser : String) : Call<ArrayList<Address>>? {
        return addressRoutes?.getAddress(idUser, token)
    }

    fun create(address: Address) : Call<ResponseHttp> ? {
        return addressRoutes?.create(address, token)
    }

}