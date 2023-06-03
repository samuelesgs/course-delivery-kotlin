package com.example.project_1.routes

import com.example.project_1.models.Order
import com.example.project_1.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrdersRoutes {


    @GET("orders/findByClientAndStatus/{id_client}/{status}")
    fun getOrdersFindByClientAndStatus(
        @Path("id_client") id_client: String,
        @Path("status") status: String,
        @Header("Authorization") token : String
    ): Call<ArrayList<Order>>

    @GET("orders/findByDeliveryAndStatus/{id_delivery}/{status}")
    fun getOrdersFindByDeliveryAndStatus(
        @Path("id_delivery") id_delivery: String,
        @Path("status") status: String,
        @Header("Authorization") token : String
    ): Call<ArrayList<Order>>

    @GET("orders/findByStatus/{status}")
    fun getOrdersFindByStatus(
        @Path("status") status: String,
        @Header("Authorization") token : String
    ): Call<ArrayList<Order>>

    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token : String
    ) : Call<ResponseHttp>

    @PUT("orders/updateToDispatched")
    fun updateToDispatched(
        @Body order: Order,
        @Header("Authorization") token : String
    ) : Call<ResponseHttp>


    @PUT("orders/updateToOnTheWay")
    fun updateToOnTheWay(
        @Body order: Order,
        @Header("Authorization") token : String
    ) : Call<ResponseHttp>

    @PUT("orders/updateToDelivered")
    fun updateToDelivered(
        @Body order: Order,
        @Header("Authorization") token : String
    ) : Call<ResponseHttp>



}