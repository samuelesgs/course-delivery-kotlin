package com.example.project_1.providers

import com.example.project_1.api.ApiRoutes
import com.example.project_1.models.Order
import com.example.project_1.models.ResponseHttp
import com.example.project_1.routes.OrdersRoutes
import retrofit2.Call

class OrderProvider(val token: String) {

    private var ordersRoutes : OrdersRoutes? = null

    init {
        val api = ApiRoutes()
        ordersRoutes = api.getOrdersRoutes(token)
    }

    fun create(order: Order) : Call<ResponseHttp> ? {
        return ordersRoutes?.create(order, token)
    }
}