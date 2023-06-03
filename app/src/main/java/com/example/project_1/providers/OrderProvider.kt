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

    fun getOrdersByStatus(status: String): Call<ArrayList<Order>>? {
        return ordersRoutes?.getOrdersFindByStatus(status, token)
    }

    fun getOrdersByClientAndStatus(idClient: String, status: String): Call<ArrayList<Order>>? {
        return ordersRoutes?.getOrdersFindByClientAndStatus(idClient, status, token)
    }

    fun getOrdersByDeliveryAndStatus(idDelivery: String, status: String): Call<ArrayList<Order>>? {
        return ordersRoutes?.getOrdersFindByDeliveryAndStatus(idDelivery, status, token)
    }

    fun create(order: Order) : Call<ResponseHttp> ? {
        return ordersRoutes?.create(order, token)
    }

    fun updateToDispatched(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.updateToDispatched(order, token)
    }

    fun updateToOnTheWay(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.updateToOnTheWay(order, token)
    }

    fun updateToDelivered(order: Order): Call<ResponseHttp >? {
        return ordersRoutes?.updateToDelivered(order, token)
    }
}