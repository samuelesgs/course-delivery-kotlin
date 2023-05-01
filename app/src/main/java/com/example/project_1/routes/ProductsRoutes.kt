package com.example.project_1.routes

import com.example.project_1.models.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductsRoutes {

    @Multipart
    @POST("products/create")
    fun create(
        @Part image : Array<MultipartBody.Part?>,
        @Part("product") category : RequestBody,
        @Header("Authorization") token : String
    ) : Call<ResponseHttp>
}