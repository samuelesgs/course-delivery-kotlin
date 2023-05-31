package com.example.project_1.routes

import com.example.project_1.models.Order
import com.example.project_1.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrdersRoutes {


    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token : String
    ) : Call<ResponseHttp>
}