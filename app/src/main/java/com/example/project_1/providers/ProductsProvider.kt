package com.example.project_1.providers

import com.example.project_1.api.ApiRoutes
import com.example.project_1.models.Product
import com.example.project_1.models.ResponseHttp
import com.example.project_1.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider(val token : String ? = null) {

    private var productsRoutes : ProductsRoutes ? = null

    init {
        val api = ApiRoutes()
        productsRoutes = api.getProductsRoutes(token!!)
    }

    fun findByCategory(id_category :  String) : Call<ArrayList<Product>> ? {
        return productsRoutes?.findByCategory(id_category, token!!)
    }

    fun create (files : List<File>, product: Product) : Call<ResponseHttp> ? {
        val images = arrayOfNulls<MultipartBody.Part>(files.size)
        for (i in 0 until files.size) {
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productsRoutes?.create(images, requestBody, token!!)
    }
}